package com.dazi.review.service;

import com.dazi.activity.entity.ActivityParticipant;
import com.dazi.activity.repository.ActivityParticipantRepository;
import com.dazi.common.result.Result;
import com.dazi.review.entity.Review;
import com.dazi.review.repository.ReviewRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.TimeUnit;

/**
 * 评价服务 - 安全增强版
 * 支持活动参与验证、防重复评价
 */
@Slf4j
@Service
@RequiredArgsConstructor
public class ReviewService {
    
    private final ReviewRepository reviewRepository;
    private final ActivityParticipantRepository participantRepository;
    private final RedisTemplate<String, Object> redisTemplate;
    
    private static final String REVIEW_CACHE_PREFIX = "review:cache:";
    private static final String REVIEW_LOCK_PREFIX = "review:lock:";
    
    /**
     * 提交评价（带活动参与验证和防重复评价）
     */
    @Transactional
    public Result<Long> submitReview(Review review) {
        Long reviewerId = review.getReviewerId();
        Long revieweeId = review.getRevieweeId();
        Long activityId = review.getActivityId();
        
        // 1. 验证是否参与了该活动
        ActivityParticipant participation = participantRepository.findByActivityIdAndUserId(activityId, reviewerId);
        if (participation == null || participation.getStatus() != 1) {
            log.warn("评价失败：用户未参与活动: reviewerId={}, activityId={}", reviewerId, activityId);
            return Result.error("您未参与该活动，无法评价");
        }
        
        // 2. 检查活动是否已结束（可选：只允许在活动结束后评价）
        // TODO: 检查活动结束时间
        
        // 3. 防重复评价锁
        String lockKey = REVIEW_LOCK_PREFIX + reviewerId + ":" + activityId + ":" + revieweeId;
        Boolean locked = redisTemplate.opsForValue().setIfAbsent(lockKey, "1", 10, TimeUnit.SECONDS);
        if (!Boolean.TRUE.equals(locked)) {
            return Result.error("评价提交中，请稍后再试");
        }
        
        try {
            // 4. 检查是否已评价过
            Review existingReview = reviewRepository.findByReviewerIdAndActivityIdAndRevieweeId(
                    reviewerId, activityId, revieweeId);
            if (existingReview != null) {
                return Result.error("您已评价过该用户");
            }
            
            // 5. 校验评分范围
            if (review.getRating() == null || review.getRating() < 1 || review.getRating() > 5) {
                return Result.error("评分必须在1-5之间");
            }
            
            // 6. 内容长度校验
            if (review.getContent() != null && review.getContent().length() > 500) {
                return Result.error("评价内容过长，最多500字符");
            }
            
            // 7. 保存评价
            reviewRepository.insert(review);
            
            // 8. 清除缓存
            clearReviewCache(revieweeId);
            
            log.info("评价提交成功: activityId={}, reviewerId={}, revieweeId={}, rating={}",
                    activityId, reviewerId, revieweeId, review.getRating());
            
            return Result.success(review.getId());
        } finally {
            redisTemplate.delete(lockKey);
        }
    }
    
    /**
     * 获取用户评价（带缓存）
     */
    public Result<List<Review>> getUserReviews(Long userId) {
        String cacheKey = REVIEW_CACHE_PREFIX + "user:" + userId;
        
        @SuppressWarnings("unchecked")
        List<Review> cached = (List<Review>) redisTemplate.opsForValue().get(cacheKey);
        if (cached != null) {
            return Result.success(cached);
        }
        
        List<Review> reviews = reviewRepository.findByRevieweeId(userId);
        
        // 缓存10分钟
        redisTemplate.opsForValue().set(cacheKey, reviews, 10, TimeUnit.MINUTES);
        
        return Result.success(reviews);
    }
    
    /**
     * 获取用户评分统计（带缓存）
     */
    public Result<Map<String, Object>> getUserRatingStats(Long userId) {
        String cacheKey = REVIEW_CACHE_PREFIX + "stats:" + userId;
        
        @SuppressWarnings("unchecked")
        Map<String, Object> cached = (Map<String, Object>) redisTemplate.opsForValue().get(cacheKey);
        if (cached != null) {
            return Result.success(cached);
        }
        
        List<Review> reviews = reviewRepository.findByRevieweeId(userId);
        Double avgRating = reviewRepository.getAverageRating(userId);
        
        // 统计各星级评价数量
        Map<Integer, Long> ratingDistribution = new HashMap<>();
        for (int i = 1; i <= 5; i++) {
            final int rating = i;
            long count = reviews.stream().filter(r -> r.getRating() == rating).count();
            ratingDistribution.put(i, count);
        }
        
        Map<String, Object> stats = new HashMap<>();
        stats.put("totalReviews", reviews.size());
        stats.put("averageRating", avgRating != null ? Math.round(avgRating * 10) / 10.0 : 0);
        stats.put("ratingDistribution", ratingDistribution);
        
        // 缓存10分钟
        redisTemplate.opsForValue().set(cacheKey, stats, 10, TimeUnit.MINUTES);
        
        return Result.success(stats);
    }
    
    /**
     * 计算信用分（带缓存）
     */
    public Result<Integer> calculateCreditScore(Long userId) {
        String cacheKey = REVIEW_CACHE_PREFIX + "credit:" + userId;
        
        Integer cached = (Integer) redisTemplate.opsForValue().get(cacheKey);
        if (cached != null) {
            return Result.success(cached);
        }
        
        List<Review> reviews = reviewRepository.findByRevieweeId(userId);
        
        int baseScore = 100;
        
        for (Review review : reviews) {
            if (review.getRating() >= 4) {
                baseScore += 3; // 好评+3
            } else if (review.getRating() == 3) {
                baseScore += 1; // 中评+1
            } else {
                baseScore -= 5; // 差评-5
            }
        }
        
        // 限制在0-100之间
        baseScore = Math.max(0, Math.min(100, baseScore));
        
        // 缓存1小时
        redisTemplate.opsForValue().set(cacheKey, baseScore, 1, TimeUnit.HOURS);
        
        return Result.success(baseScore);
    }
    
    /**
     * 清除评价缓存
     */
    private void clearReviewCache(Long userId) {
        redisTemplate.delete(REVIEW_CACHE_PREFIX + "user:" + userId);
        redisTemplate.delete(REVIEW_CACHE_PREFIX + "stats:" + userId);
        redisTemplate.delete(REVIEW_CACHE_PREFIX + "credit:" + userId);
    }
}
