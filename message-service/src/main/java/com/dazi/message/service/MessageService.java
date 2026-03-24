package com.dazi.message.service;

import com.dazi.common.result.Result;
import com.dazi.message.entity.Conversation;
import com.dazi.message.entity.Message;
import com.dazi.message.repository.ConversationRepository;
import com.dazi.message.repository.MessageRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.StringUtils;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.time.LocalDateTime;
import java.util.*;
import java.util.regex.Pattern;

/**
 * 消息服务 - 安全增强版
 * 支持内容过滤、敏感词检测
 */
@Slf4j
@Service
@RequiredArgsConstructor
public class MessageService {
    
    private final MessageRepository messageRepository;
    private final ConversationRepository conversationRepository;
    private final RedisTemplate<String, Object> redisTemplate;
    
    @Value("${file.upload.path:/tmp/uploads}")
    private String uploadPath;
    
    @Value("${file.upload.url-prefix:http://localhost:8080/uploads}")
    private String uploadUrlPrefix;
    
    // 敏感词列表（实际应该从配置文件或数据库加载）
    private static final Set<String> SENSITIVE_WORDS = Set.of(
        "诈骗", "赌博", "色情", "暴力", "毒品", "枪支", "反动",
        "fuck", "shit", "damn", "bitch"
    );
    
    // 内容最大长度
    private static final int MAX_CONTENT_LENGTH = 500;
    
    // HTML标签正则
    private static final Pattern HTML_PATTERN = Pattern.compile("<[^>]*>");
    
    // 支持的图片格式
    private static final Set<String> IMAGE_TYPES = Set.of("image/jpeg", "image/png", "image/gif", "image/webp");
    
    // 支持的视频/音频格式
    private static final Set<String> VOICE_TYPES = Set.of("audio/mpeg", "audio/mp3", "audio/wav", "audio/amr", "audio/aac");
    
    /**
     * 发送消息（带内容过滤）
     */
    @Transactional
    public Result<Long> sendMessage(Long senderId, Long receiverId, String content, String messageType, Integer type) {
        // 1. 参数校验
        if (!StringUtils.hasText(content) && !"voice".equals(messageType) && !"image".equals(messageType)) {
            return Result.error("消息内容不能为空");
        }
        
        // 2. 内容长度校验
        if (content != null && content.length() > MAX_CONTENT_LENGTH) {
            return Result.error("消息内容过长，最多" + MAX_CONTENT_LENGTH + "字符");
        }
        
        // 3. XSS过滤
        if (content != null) {
            content = sanitizeContent(content);
        }
        
        // 4. 敏感词检测
        if (content != null) {
            SensitiveCheckResult checkResult = checkSensitiveWords(content);
            if (checkResult.hasSensitiveWords()) {
                log.warn("消息包含敏感词: senderId={}, words={}", senderId, checkResult.getFoundWords());
                content = checkResult.getFilteredContent();
            }
        }
        
        // 5. 防刷屏检查
        if (isSpamming(senderId)) {
            return Result.error("发送消息过于频繁，请稍后再试");
        }
        
        // 6. 保存消息
        Message message = new Message();
        message.setSenderId(senderId);
        message.setReceiverId(receiverId);
        message.setType(type);
        message.setMessageType(messageType != null ? messageType : "text");
        message.setContent(content);
        message.setStatus(0); // 未读
        messageRepository.insert(message);
        
        // 7. 更新会话
        updateConversation(senderId, receiverId, content);
        updateConversation(receiverId, senderId, content);
        
        // 8. 记录发送频率
        recordMessageSent(senderId);
        
        log.info("消息发送成功: from={}, to={}, msgId={}, type={}", senderId, receiverId, message.getId(), messageType);
        
        return Result.success(message.getId());
    }
    
    /**
     * 获取聊天记录
     */
    public Result<List<Message>> getConversation(Long userId, Long targetId, Integer page, Integer pageSize) {
        // 参数校验
        if (page == null || page < 1) page = 1;
        if (pageSize == null || pageSize < 1 || pageSize > 100) pageSize = 20;
        
        int offset = (page - 1) * pageSize;
        List<Message> messages = messageRepository.findConversationWithPagination(userId, targetId, offset, pageSize);
        
        // 标记为已读（异步处理更好）
        messages.forEach(msg -> {
            if (msg.getReceiverId().equals(userId) && msg.getStatus() == 0) {
                msg.setStatus(1);
                msg.setReadTime(LocalDateTime.now());
                messageRepository.updateById(msg);
            }
        });
        
        return Result.success(messages);
    }
    
    /**
     * 获取会话列表
     */
    public Result<List<Conversation>> getConversations(Long userId) {
        List<Conversation> conversations = conversationRepository.findByUserId(userId);
        return Result.success(conversations);
    }
    
    /**
     * 获取未读消息数
     */
    public Result<Map<String, Object>> getUnreadCount(Long userId) {
        Integer count = messageRepository.countUnread(userId);
        Map<String, Object> result = new HashMap<>();
        result.put("unreadCount", count);
        return Result.success(result);
    }
    
    /**
     * 标记会话已读
     */
    @Transactional
    public Result<Void> markAsRead(Long userId, Long conversationId) {
        // 将会话中所有消息标记为已读
        messageRepository.markConversationAsRead(userId, conversationId);
        
        // 更新会话未读数
        Conversation conversation = conversationRepository.selectById(conversationId);
        if (conversation != null && conversation.getUserId().equals(userId)) {
            conversation.setUnreadCount(0);
            conversationRepository.updateById(conversation);
        }
        
        return Result.success();
    }
    
    /**
     * 标记用户消息已读
     */
    @Transactional
    public Result<Void> markUserMessagesAsRead(Long userId, Long targetUserId) {
        messageRepository.markMessagesAsReadByUserId(userId, targetUserId);
        return Result.success();
    }
    
    /**
     * 上传语音
     */
    @Transactional
    public Result<Map<String, Object>> uploadVoice(Long userId, MultipartFile file, Integer duration) {
        // 校验文件
        if (file == null || file.isEmpty()) {
            return Result.error("语音文件不能为空");
        }
        
        String contentType = file.getContentType();
        if (!VOICE_TYPES.contains(contentType)) {
            return Result.error("不支持的语音格式，请上传 MP3, WAV, AMR, AAC 格式");
        }
        
        // 限制文件大小 (10MB)
        if (file.getSize() > 10 * 1024 * 1024) {
            return Result.error("语音文件过大，最大支持10MB");
        }
        
        // 限制时长 (5分钟)
        if (duration != null && duration > 300) {
            return Result.error("语音时长过长，最大支持5分钟");
        }
        
        try {
            // 生成文件名
            String extension = getExtension(file.getOriginalFilename());
            String fileName = UUID.randomUUID().toString() + "." + extension;
            String datePath = LocalDateTime.now().format(java.time.format.DateTimeFormatter.ofPattern("yyyy/MM/dd"));
            String relativePath = "/voice/" + datePath + "/" + fileName;
            String fullPath = uploadPath + relativePath;
            
            // 创建目录
            Path path = Paths.get(fullPath);
            Files.createDirectories(path.getParent());
            
            // 保存文件
            file.transferTo(path.toFile());
            
            // 构建URL
            String fileUrl = uploadUrlPrefix + relativePath;
            
            Map<String, Object> result = new HashMap<>();
            result.put("url", fileUrl);
            result.put("duration", duration);
            result.put("size", file.getSize());
            
            log.info("语音上传成功: userId={}, url={}", userId, fileUrl);
            
            return Result.success(result);
            
        } catch (IOException e) {
            log.error("语音上传失败: userId={}, error={}", userId, e.getMessage());
            return Result.error("语音上传失败: " + e.getMessage());
        }
    }
    
    /**
     * 上传图片
     */
    @Transactional
    public Result<Map<String, Object>> uploadImage(Long userId, MultipartFile file) {
        // 校验文件
        if (file == null || file.isEmpty()) {
            return Result.error("图片文件不能为空");
        }
        
        String contentType = file.getContentType();
        if (!IMAGE_TYPES.contains(contentType)) {
            return Result.error("不支持的图片格式，请上传 JPEG, PNG, GIF, WEBP 格式");
        }
        
        // 限制文件大小 (5MB)
        if (file.getSize() > 5 * 1024 * 1024) {
            return Result.error("图片文件过大，最大支持5MB");
        }
        
        try {
            // 生成文件名
            String extension = getExtension(file.getOriginalFilename());
            String fileName = UUID.randomUUID().toString() + "." + extension;
            String datePath = LocalDateTime.now().format(java.time.format.DateTimeFormatter.ofPattern("yyyy/MM/dd"));
            String relativePath = "/image/" + datePath + "/" + fileName;
            String fullPath = uploadPath + relativePath;
            
            // 创建目录
            Path path = Paths.get(fullPath);
            Files.createDirectories(path.getParent());
            
            // 保存文件
            file.transferTo(path.toFile());
            
            // 构建URL
            String fileUrl = uploadUrlPrefix + relativePath;
            
            Map<String, Object> result = new HashMap<>();
            result.put("url", fileUrl);
            result.put("size", file.getSize());
            
            log.info("图片上传成功: userId={}, url={}", userId, fileUrl);
            
            return Result.success(result);
            
        } catch (IOException e) {
            log.error("图片上传失败: userId={}, error={}", userId, e.getMessage());
            return Result.error("图片上传失败: " + e.getMessage());
        }
    }
    
    /**
     * 获取文件扩展名
     */
    private String getExtension(String filename) {
        if (filename == null || !filename.contains(".")) {
            return "bin";
        }
        return filename.substring(filename.lastIndexOf(".") + 1).toLowerCase();
    }
    
    /**
     * 内容XSS过滤
     */
    private String sanitizeContent(String content) {
        if (content == null) {
            return "";
        }
        
        // 1. 去除HTML标签
        content = HTML_PATTERN.matcher(content).replaceAll("");
        
        // 2. 转义特殊字符
        content = content.replace("&", "&amp;")
                        .replace("<", "&lt;")
                        .replace(">", "&gt;")
                        .replace("\"", "&quot;")
                        .replace("'", "&#x27;")
                        .replace("/", "&#x2F;");
        
        return content;
    }
    
    /**
     * 敏感词检测
     */
    private SensitiveCheckResult checkSensitiveWords(String content) {
        String filteredContent = content;
        Set<String> foundWords = new java.util.HashSet<>();
        
        for (String word : SENSITIVE_WORDS) {
            if (content.toLowerCase().contains(word.toLowerCase())) {
                foundWords.add(word);
                // 使用*替换敏感词
                String replacement = "*".repeat(word.length());
                filteredContent = filteredContent.replaceAll("(?i)" + Pattern.quote(word), replacement);
            }
        }
        
        return new SensitiveCheckResult(!foundWords.isEmpty(), foundWords, filteredContent);
    }
    
    /**
     * 防刷屏检查
     */
    private boolean isSpamming(Long userId) {
        String key = "message:rate:" + userId;
        String countStr = (String) redisTemplate.opsForValue().get(key);
        
        if (countStr != null) {
            int count = Integer.parseInt(countStr);
            // 1分钟内超过10条消息视为刷屏
            return count >= 10;
        }
        
        return false;
    }
    
    /**
     * 记录消息发送
     */
    private void recordMessageSent(Long userId) {
        String key = "message:rate:" + userId;
        
        Long count = redisTemplate.opsForValue().increment(key);
        if (count != null && count == 1) {
            // 设置1分钟过期
            redisTemplate.expire(key, 60, java.util.concurrent.TimeUnit.SECONDS);
        }
    }
    
    /**
     * 更新会话
     */
    private void updateConversation(Long userId, Long targetId, String lastMessage) {
        // 截断过长的消息预览
        String preview = lastMessage != null && lastMessage.length() > 50 
            ? lastMessage.substring(0, 50) + "..." 
            : lastMessage;
        
        // 查找现有会话
        Conversation conversation = conversationRepository.findByUserIdAndTargetId(userId, targetId);
        
        if (conversation == null) {
            // 创建新会话
            conversation = new Conversation();
            conversation.setUserId(userId);
            conversation.setTargetId(targetId);
            conversation.setType(1); // 私聊
            conversation.setUnreadCount(0);
            conversationRepository.insert(conversation);
        }
        
        // 更新会话
        conversation.setLastMessage(preview);
        conversation.setLastMessageTime(LocalDateTime.now());
        if (!userId.equals(targetId)) {
            conversation.setUnreadCount(conversation.getUnreadCount() + 1);
        }
        conversationRepository.updateById(conversation);
    }
    
    /**
     * 敏感词检测结果
     */
    private record SensitiveCheckResult(boolean hasSensitiveWords, 
                                        Set<String> foundWords, 
                                        String filteredContent) {
    }
}
