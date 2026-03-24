package com.dazi.moment.service;

import com.dazi.common.result.Result;
import com.dazi.moment.entity.Moment;
import com.dazi.moment.entity.MomentComment;
import com.dazi.moment.entity.MomentLike;
import com.dazi.moment.repository.MomentCommentRepository;
import com.dazi.moment.repository.MomentLikeRepository;
import com.dazi.moment.repository.MomentRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.StringUtils;

import java.time.LocalDateTime;
import java.time.temporal.ChronoUnit;
import java.util.*;
import java.util.regex.Pattern;
import java.util.stream.Collectors;

@Slf4j
@Service
@RequiredArgsConstructor
public class MomentService {
    
    private final MomentRepository momentRepository;
    private final MomentLikeRepository momentLikeRepository;
    private final MomentCommentRepository momentCommentRepository;
    
    // 敏感词列表
    private static final Set<String> SENSITIVE_WORDS = Set.of(
        "诈骗", "赌博", "色情", "暴力", "毒品", "枪支", "反动",
        "fuck", "shit", "damn", "bitch"
    );
    
    private static final int MAX_CONTENT_LENGTH = 1000;
    private static final int MAX_COMMENT_LENGTH = 500;
    private static final Pattern HTML_PATTERN = Pattern.compile("<[^>]*>");
    
    /**
     * 发布动态
     */
    @Transactional
    public Result<Long> publishMoment(Long userId, String content, String images, String location) {
        // 参数校验
        if (!StringUtils.hasText(content)) {
            return Result.error("动态内容不能为空");
        }
        
        if (content.length() > MAX_CONTENT_LENGTH) {
            return Result.error("动态内容过长，最多" + MAX_CONTENT_LENGTH + "字符");
        }
        
        // XSS过滤
        content = sanitizeContent(content);
        
        // 敏感词检测
        SensitiveCheckResult checkResult = checkSensitiveWords(content);
        if (checkResult.hasSensitiveWords()) {
            log.warn("动态包含敏感词: userId={}, words={}", userId, checkResult.getFoundWords());
            content = checkResult.getFilteredContent();
        }
        
        Moment moment = new Moment();
        moment.setUserId(userId);
        moment.setContent(content);
        moment.setImages(images);
        moment.setLocation(location);
        moment.setLikeCount(0);
        moment.setCommentCount(0);
        moment.setViewCount(0);
        moment.setIsTop(0);
        moment.setStatus(1);
        
        momentRepository.insert(moment);
        
        log.info("动态发布成功: userId={}, momentId={}", userId, moment.getId());
        
        return Result.success(moment.getId());
    }
    
    /**
     * 获取动态列表
     */
    public Result<List<Map<String, Object>>> getMomentList(Long currentUserId, String type, Integer page, Integer pageSize) {
        if (page == null || page < 1) page = 1;
        if (pageSize == null || pageSize < 1 || pageSize > 50) pageSize = 20;
        
        int offset = (page - 1) * pageSize;
        List<Moment> moments;
        
        if ("following".equals(type)) {
            // 获取关注的人的动态
            moments = momentRepository.findFollowingMoments(currentUserId, offset, pageSize);
        } else {
            // 获取所有动态
            moments = momentRepository.findAllWithPagination(offset, pageSize);
        }
        
        List<Map<String, Object>> result = new ArrayList<>();
        for (Moment moment : moments) {
            Map<String, Object> map = convertToMap(moment, currentUserId);
            result.add(map);
        }
        
        return Result.success(result);
    }
    
    /**
     * 获取动态详情
     */
    public Result<Map<String, Object>> getMomentDetail(Long momentId, Long currentUserId) {
        Moment moment = momentRepository.selectById(momentId);
        if (moment == null || moment.getDeleted() == 1) {
            return Result.error("动态不存在");
        }
        
        if (moment.getStatus() != 1) {
            return Result.error("动态已被禁用");
        }
        
        // 增加浏览量
        momentRepository.incrementViewCount(momentId);
        moment.setViewCount(moment.getViewCount() + 1);
        
        return Result.success(convertToMap(moment, currentUserId));
    }
    
    /**
     * 点赞动态
     */
    @Transactional
    public Result<Void> likeMoment(Long userId, Long momentId) {
        Moment moment = momentRepository.selectById(momentId);
        if (moment == null || moment.getDeleted() == 1) {
            return Result.error("动态不存在");
        }
        
        // 检查是否已经点赞
        Integer exists = momentLikeRepository.existsByMomentIdAndUserId(momentId, userId);
        if (exists != null && exists > 0) {
            return Result.error("已经点赞过了");
        }
        
        MomentLike like = new MomentLike();
        like.setMomentId(momentId);
        like.setUserId(userId);
        momentLikeRepository.insert(like);
        
        momentRepository.incrementLikeCount(momentId);
        
        log.info("动态点赞成功: userId={}, momentId={}", userId, momentId);
        
        return Result.success();
    }
    
    /**
     * 取消点赞
     */
    @Transactional
    public Result<Void> unlikeMoment(Long userId, Long momentId) {
        MomentLike like = momentLikeRepository.findByMomentIdAndUserId(momentId, userId);
        if (like == null) {
            return Result.error("尚未点赞");
        }
        
        momentLikeRepository.deleteById(like.getId());
        momentRepository.decrementLikeCount(momentId);
        
        log.info("动态取消点赞: userId={}, momentId={}", userId, momentId);
        
        return Result.success();
    }
    
    /**
     * 评论动态
     */
    @Transactional
    public Result<Long> commentMoment(Long userId, Long momentId, Long parentId, String content) {
        Moment moment = momentRepository.selectById(momentId);
        if (moment == null || moment.getDeleted() == 1) {
            return Result.error("动态不存在");
        }
        
        if (!StringUtils.hasText(content)) {
            return Result.error("评论内容不能为空");
        }
        
        if (content.length() > MAX_COMMENT_LENGTH) {
            return Result.error("评论内容过长，最多" + MAX_COMMENT_LENGTH + "字符");
        }
        
        // XSS过滤
        content = sanitizeContent(content);
        
        // 敏感词检测
        SensitiveCheckResult checkResult = checkSensitiveWords(content);
        if (checkResult.hasSensitiveWords()) {
            log.warn("评论包含敏感词: userId={}, words={}", userId, checkResult.getFoundWords());
            content = checkResult.getFilteredContent();
        }
        
        MomentComment comment = new MomentComment();
        comment.setMomentId(momentId);
        comment.setUserId(userId);
        comment.setParentId(parentId);
        comment.setContent(content);
        comment.setLikeCount(0);
        comment.setStatus(1);
        
        momentCommentRepository.insert(comment);
        
        // 增加评论数
        momentRepository.incrementCommentCount(momentId);
        
        log.info("动态评论成功: userId={}, momentId={}, commentId={}", userId, momentId, comment.getId());
        
        return Result.success(comment.getId());
    }
    
    /**
     * 获取评论列表
     */
    public Result<List<Map<String, Object>>> getComments(Long momentId, Integer page, Integer pageSize) {
        if (page == null || page < 1) page = 1;
        if (pageSize == null || pageSize < 1 || pageSize > 50) pageSize = 20;
        
        int offset = (page - 1) * pageSize;
        
        List<MomentComment> rootComments = momentCommentRepository.findRootCommentsByMomentId(momentId, offset, pageSize);
        
        List<Map<String, Object>> result = new ArrayList<>();
        for (MomentComment comment : rootComments) {
            Map<String, Object> map = convertCommentToMap(comment);
            
            // 获取回复
            List<MomentComment> replies = momentCommentRepository.findRepliesByParentId(comment.getId());
            List<Map<String, Object>> replyList = new ArrayList<>();
            for (MomentComment reply : replies) {
                replyList.add(convertCommentToMap(reply));
            }
            map.put("replies", replyList);
            map.put("replyCount", replies.size());
            
            result.add(map);
        }
        
        return Result.success(result);
    }
    
    /**
     * 删除动态
     */
    @Transactional
    public Result<Void> deleteMoment(Long userId, Long momentId) {
        Moment moment = momentRepository.selectById(momentId);
        if (moment == null || moment.getDeleted() == 1) {
            return Result.error("动态不存在");
        }
        
        if (!moment.getUserId().equals(userId)) {
            return Result.error("无权删除此动态");
        }
        
        momentRepository.deleteById(momentId);
        
        log.info("动态删除成功: userId={}, momentId={}", userId, momentId);
        
        return Result.success();
    }
    
    /**
     * 获取热门动态
     * 热度算法：热度 = (点赞数 * 1 + 评论数 * 2 + 浏览数 * 0.1) * 时间衰减系数
     */
    public Result<List<Map<String, Object>>> getHotMoments(Long userId, Integer page, Integer pageSize) {
        if (page == null || page < 1) page = 1;
        if (pageSize == null || pageSize < 1 || pageSize > 50) pageSize = 20;
        
        // 获取所有活跃动态
        List<Moment> moments = momentRepository.findAllActiveMoments();
        
        // 计算热度分数
        List<Map<String, Object>> hotMoments = new ArrayList<>();
        for (Moment moment : moments) {
            double hotScore = calculateHotScore(moment);
            Map<String, Object> map = convertToMap(moment, userId);
            map.put("hotScore", hotScore);
            hotMoments.add(map);
        }
        
        // 按热度排序
        hotMoments.sort((a, b) -> ((Double) b.get("hotScore")).compareTo((Double) a.get("hotScore")));
        
        // 分页
        int start = (page - 1) * pageSize;
        int end = Math.min(start + pageSize, hotMoments.size());
        
        if (start >= hotMoments.size()) {
            return Result.success(new ArrayList<>());
        }
        
        List<Map<String, Object>> result = hotMoments.subList(start, end);
        return Result.success(result);
    }
    
    /**
     * 计算热度分数
     * 热度 = (点赞数 * 1 + 评论数 * 2 + 浏览数 * 0.1) * 时间衰减系数
     * 时间衰减系数 = max(0.1, 1 - 0.1 * days)
     */
    private double calculateHotScore(Moment moment) {
        double baseScore = moment.getLikeCount() * 1.0 
                         + moment.getCommentCount() * 2.0 
                         + moment.getViewCount() * 0.1;
        
        // 时间衰减
        long days = ChronoUnit.DAYS.between(moment.getCreatedAt(), LocalDateTime.now());
        double decayFactor = Math.max(0.1, 1.0 - 0.1 * days);
        
        return baseScore * decayFactor;
    }
    
    /**
     * 转换为Map
     */
    private Map<String, Object> convertToMap(Moment moment, Long currentUserId) {
        Map<String, Object> map = new HashMap<>();
        map.put("id", moment.getId());
        map.put("userId", moment.getUserId());
        map.put("content", moment.getContent());
        map.put("images", moment.getImages());
        map.put("location", moment.getLocation());
        map.put("likeCount", moment.getLikeCount());
        map.put("commentCount", moment.getCommentCount());
        map.put("viewCount", moment.getViewCount());
        map.put("isTop", moment.getIsTop());
        map.put("createdAt", moment.getCreatedAt());
        
        // 检查当前用户是否点赞
        if (currentUserId != null) {
            Integer liked = momentLikeRepository.existsByMomentIdAndUserId(moment.getId(), currentUserId);
            map.put("isLiked", liked != null && liked > 0);
        } else {
            map.put("isLiked", false);
        }
        
        return map;
    }
    
    /**
     * 评论转换为Map
     */
    private Map<String, Object> convertCommentToMap(MomentComment comment) {
        Map<String, Object> map = new HashMap<>();
        map.put("id", comment.getId());
        map.put("momentId", comment.getMomentId());
        map.put("userId", comment.getUserId());
        map.put("parentId", comment.getParentId());
        map.put("content", comment.getContent());
        map.put("likeCount", comment.getLikeCount());
        map.put("createdAt", comment.getCreatedAt());
        return map;
    }
    
    /**
     * 内容XSS过滤
     */
    private String sanitizeContent(String content) {
        if (content == null) {
            return "";
        }
        content = HTML_PATTERN.matcher(content).replaceAll("");
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
        Set<String> foundWords = new HashSet<>();
        
        for (String word : SENSITIVE_WORDS) {
            if (content.toLowerCase().contains(word.toLowerCase())) {
                foundWords.add(word);
                String replacement = "*".repeat(word.length());
                filteredContent = filteredContent.replaceAll("(?i)" + Pattern.quote(word), replacement);
            }
        }
        
        return new SensitiveCheckResult(!foundWords.isEmpty(), foundWords, filteredContent);
    }
    
    private record SensitiveCheckResult(boolean hasSensitiveWords, 
                                        Set<String> foundWords, 
                                        String filteredContent) {
    }
}
