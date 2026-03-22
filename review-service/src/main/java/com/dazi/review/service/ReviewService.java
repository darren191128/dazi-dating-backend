package com.dazi.review.service;

import com.dazi.review.entity.Review;
import com.dazi.review.repository.ReviewRepository;
import com.dazi.common.result.Result;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Slf4j
@Service
@RequiredArgsConstructor
public class ReviewService {
    
    private final ReviewRepository reviewRepository;
    
    /**
     * 提交评价
     */
    public Result<Long> submitReview(Review review) {
        reviewRepository.insert(review);
        
        log.info("评价提交: activityId={}, reviewerId={}, revieweeId={}, rating={}",
                review.getActivityId(), review.getReviewerId(), review.getRevieweeId(), review.getRating());
        
        return Result.success(review.getId());
    }
    
    /**
     * 获取用户评价
     */
    public Result<List<Review>> getUserReviews(Long userId) {
        List<Review> reviews = reviewRepository.findByRevieweeId(userId);
        return Result.success(reviews);
    }
    
    /**
     * 获取用户评分统计
     */
    public Result<Map<String, Object>> getUserRatingStats(Long userId) {
        List<Review> reviews = reviewRepository.findByRevieweeId(userId);
        Double avgRating = reviewRepository.getAverageRating(userId);
        
        Map<String, Object> stats = new HashMap<>();
        stats.put("totalReviews", reviews.size());
        stats.put("averageRating", avgRating != null ? Math.round(avgRating * 10) / 10.0 : 0);
        
        return Result.success(stats);
    }
    
    /**
     * 计算信用分
     */
    public Result<Integer> calculateCreditScore(Long userId) {
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
        
        return Result.success(baseScore);
    }
}
