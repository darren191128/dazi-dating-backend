package com.dazi.match.controller;

import com.dazi.common.result.Result;
import com.dazi.match.service.MatchService;
import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.math.BigDecimal;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/match")
@RequiredArgsConstructor
public class MatchController {
    
    private final MatchService matchService;
    
    /**
     * 获取推荐匹配（支持高级筛选）
     */
    @GetMapping("/recommendations")
    public Result<List<Map<String, Object>>> getRecommendations(
            HttpServletRequest request,
            @RequestParam(required = false) Integer minHeight,
            @RequestParam(required = false) Integer maxHeight,
            @RequestParam(required = false) String education,
            @RequestParam(required = false) String occupation,
            @RequestParam(required = false) Integer minIncome,
            @RequestParam(required = false) Integer maxIncome,
            @RequestParam(required = false) Boolean hasHouse,
            @RequestParam(required = false) Boolean hasCar,
            @RequestParam(required = false) String maritalStatus,
            @RequestParam(defaultValue = "1") Integer page,
            @RequestParam(defaultValue = "10") Integer pageSize) {
        
        Long userId = (Long) request.getAttribute("currentUserId");
        return matchService.getRecommendations(userId, minHeight, maxHeight, education, 
                occupation, minIncome, maxIncome, hasHouse, hasCar, maritalStatus, page, pageSize);
    }
    
    /**
     * 获取每日精选
     */
    @GetMapping("/daily-picks")
    public Result<List<Map<String, Object>>> getDailyPicks(
            HttpServletRequest request,
            @RequestParam(defaultValue = "10") Integer limit) {
        
        Long userId = (Long) request.getAttribute("currentUserId");
        return matchService.getDailyPicks(userId, limit);
    }
    
    /**
     * 获取附近的人
     */
    @GetMapping("/nearby")
    public Result<List<Map<String, Object>>> getNearbyUsers(
            HttpServletRequest request,
            @RequestParam BigDecimal longitude,
            @RequestParam BigDecimal latitude,
            @RequestParam(defaultValue = "10.0") Double radiusKm,
            @RequestParam(defaultValue = "1") Integer page,
            @RequestParam(defaultValue = "10") Integer pageSize) {
        
        Long userId = (Long) request.getAttribute("currentUserId");
        return matchService.getNearbyUsers(userId, longitude, latitude, radiusKm, page, pageSize);
    }
    
    /**
     * 获取用户详情
     */
    @GetMapping("/user/{targetUserId}")
    public Result<Map<String, Object>> getUserDetail(
            HttpServletRequest request,
            @PathVariable Long targetUserId) {
        
        Long userId = (Long) request.getAttribute("currentUserId");
        return matchService.getUserDetail(userId, targetUserId);
    }
}
