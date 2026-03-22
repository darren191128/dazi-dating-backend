package com.dazi.match.controller;

import com.dazi.common.result.Result;
import com.dazi.match.service.MatchService;
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
     * 获取推荐匹配
     */
    @PostMapping("/recommend")
    public Result<List<Map<String, Object>>> getRecommendations(
            @RequestParam Long userId,
            @RequestParam BigDecimal longitude,
            @RequestParam BigDecimal latitude,
            @RequestBody List<String> interests,
            @RequestParam String zodiac,
            @RequestParam String chineseZodiac,
            @RequestParam Integer age,
            @RequestParam Integer gender,
            @RequestBody List<Map<String, Object>> targetUsers) {
        
        return matchService.calculateMatchScore(
                userId, longitude, latitude, interests, zodiac, chineseZodiac, age, gender, targetUsers);
    }
    
    /**
     * 获取附近的人
     */
    @GetMapping("/nearby")
    public Result<List<Map<String, Object>>> getNearbyUsers(
            @RequestParam BigDecimal longitude,
            @RequestParam BigDecimal latitude,
            @RequestParam(defaultValue = "10.0") Double radiusKm) {
        
        return matchService.getNearbyUsers(longitude, latitude, radiusKm);
    }
}
