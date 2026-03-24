package com.dazi.review.controller;

import com.dazi.common.result.Result;
import com.dazi.review.entity.Review;
import com.dazi.review.service.ReviewService;
import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/review")
@RequiredArgsConstructor
public class ReviewController {
    
    private final ReviewService reviewService;
    
    /**
     * 提交评价
     */
    @PostMapping
    public Result<Long> submitReview(
            HttpServletRequest request,
            @RequestBody Review review) {
        
        Long userId = (Long) request.getAttribute("currentUserId");
        review.setReviewerId(userId);
        
        return reviewService.submitReview(review);
    }
    
    /**
     * 获取用户评价
     */
    @GetMapping("/list")
    public Result<List<Review>> getReviews(
            @RequestParam(required = false) Long userId) {
        
        if (userId == null) {
            return Result.error("用户ID不能为空");
        }
        
        return reviewService.getUserReviews(userId);
    }
    
    /**
     * 获取我的评价
     */
    @GetMapping("/my")
    public Result<List<Review>> getMyReviews(HttpServletRequest request) {
        Long userId = (Long) request.getAttribute("currentUserId");
        return reviewService.getUserReviews(userId);
    }
    
    /**
     * 获取用户评分统计
     */
    @GetMapping("/stats/{userId}")
    public Result<Map<String, Object>> getUserRatingStats(@PathVariable Long userId) {
        return reviewService.getUserRatingStats(userId);
    }
    
    /**
     * 计算信用分
     */
    @GetMapping("/credit/{userId}")
    public Result<Integer> calculateCreditScore(@PathVariable Long userId) {
        return reviewService.calculateCreditScore(userId);
    }
}
