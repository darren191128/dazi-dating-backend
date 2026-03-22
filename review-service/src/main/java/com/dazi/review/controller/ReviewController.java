package com.dazi.review.controller;

import com.dazi.common.result.Result;
import com.dazi.review.entity.Review;
import com.dazi.review.service.ReviewService;
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
    @PostMapping("/submit")
    public Result<Long> submitReview(@RequestBody Review review) {
        return reviewService.submitReview(review);
    }
    
    /**
     * 获取用户评价
     */
    @GetMapping("/user/{userId}")
    public Result<List<Review>> getUserReviews(@PathVariable Long userId) {
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
