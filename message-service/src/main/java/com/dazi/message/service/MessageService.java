package com.dazi.message.service;

import com.dazi.message.entity.Conversation;
import com.dazi.message.entity.Message;
import com.dazi.message.repository.ConversationRepository;
import com.dazi.message.repository.MessageRepository;
import com.dazi.common.result.Result;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Slf4j
@Service
@RequiredArgsConstructor
public class MessageService {
    
    private final MessageRepository messageRepository;
    private final ConversationRepository conversationRepository;
    
    /**
     * 发送消息
     */
    @Transactional
    public Result<Long> sendMessage(Long senderId, Long receiverId, String content, Integer type) {
        // 保存消息
        Message message = new Message();
        message.setSenderId(senderId);
        message.setReceiverId(receiverId);
        message.setType(type);
        message.setContent(content);
        message.setStatus(0); // 未读
        messageRepository.insert(message);
        
        // 更新或创建会话
        updateConversation(senderId, receiverId, content);
        updateConversation(receiverId, senderId, content);
        
        log.info("消息发送: from={}, to={}, content={}", senderId, receiverId, content);
        
        return Result.success(message.getId());
    }
    
    /**
     * 获取聊天记录
     */
    public Result<List<Message>> getConversation(Long userId, Long targetId) {
        List<Message> messages = messageRepository.findConversation(userId, targetId);
        
        // 标记为已读
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
     * 更新会话
     */
    private void updateConversation(Long userId, Long targetId, String lastMessage) {
        // 查找现有会话
        List<Conversation> conversations = conversationRepository.findByUserId(userId);
        Conversation conversation = conversations.stream()
                .filter(c -> c.getTargetId().equals(targetId))
                .findFirst()
                .orElse(null);
        
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
        conversation.setLastMessage(lastMessage);
        conversation.setLastMessageTime(LocalDateTime.now());
        if (!userId.equals(targetId)) {
            conversation.setUnreadCount(conversation.getUnreadCount() + 1);
        }
        conversationRepository.updateById(conversation);
    }
}
