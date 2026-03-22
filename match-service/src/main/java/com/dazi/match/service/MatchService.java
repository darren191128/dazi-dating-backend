package com.dazi.match.service;

import com.dazi.common.result.Result;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.*;

@Slf4j
@Service
@RequiredArgsConstructor
public class MatchService {
    
    private final RedisTemplate<String, Object> redisTemplate;
    
    // 星座相容性评分表
    private static final Map<String, Map<String, Integer>> ZODIAC_COMPATIBILITY = new HashMap<>();
    
    static {
        // 白羊座最佳配对：狮子座、射手座
        ZODIAC_COMPATIBILITY.put("白羊座", Map.of("狮子座", 100, "射手座", 100, "双子座", 80, "水瓶座", 70));
        // 金牛座最佳配对：处女座、摩羯座
        ZODIAC_COMPATIBILITY.put("金牛座", Map.of("处女座", 100, "摩羯座", 100, "巨蟹座", 80, "双鱼座", 70));
        // 双子座最佳配对：天秤座、水瓶座
        ZODIAC_COMPATIBILITY.put("双子座", Map.of("天秤座", 100, "水瓶座", 100, "白羊座", 80, "狮子座", 70));
        // 巨蟹座最佳配对：天蝎座、双鱼座
        ZODIAC_COMPATIBILITY.put("巨蟹座", Map.of("天蝎座", 100, "双鱼座", 100, "金牛座", 80, "处女座", 70));
        // 狮子座最佳配对：白羊座、射手座
        ZODIAC_COMPATIBILITY.put("狮子座", Map.of("白羊座", 100, "射手座", 100, "双子座", 80, "天秤座", 70));
        // 处女座最佳配对：金牛座、摩羯座
        ZODIAC_COMPATIBILITY.put("处女座", Map.of("金牛座", 100, "摩羯座", 100, "巨蟹座", 80, "天蝎座", 70));
        // 天秤座最佳配对：双子座、水瓶座
        ZODIAC_COMPATIBILITY.put("天秤座", Map.of("双子座", 100, "水瓶座", 100, "狮子座", 80, "射手座", 70));
        // 天蝎座最佳配对：巨蟹座、双鱼座
        ZODIAC_COMPATIBILITY.put("天蝎座", Map.of("巨蟹座", 100, "双鱼座", 100, "处女座", 80, "摩羯座", 70));
        // 射手座最佳配对：白羊座、狮子座
        ZODIAC_COMPATIBILITY.put("射手座", Map.of("白羊座", 100, "狮子座", 100, "天秤座", 80, "水瓶座", 70));
        // 摩羯座最佳配对：金牛座、处女座
        ZODIAC_COMPATIBILITY.put("摩羯座", Map.of("金牛座", 100, "处女座", 100, "天蝎座", 80, "双鱼座", 70));
        // 水瓶座最佳配对：双子座、天秤座
        ZODIAC_COMPATIBILITY.put("水瓶座", Map.of("双子座", 100, "天秤座", 100, "白羊座", 80, "射手座", 70));
        // 双鱼座最佳配对：巨蟹座、天蝎座
        ZODIAC_COMPATIBILITY.put("双鱼座", Map.of("巨蟹座", 100, "天蝎座", 100, "金牛座", 80, "摩羯座", 70));
    }
    
    /**
     * 计算匹配分数
     */
    public Result<List<Map<String, Object>>> calculateMatchScore(
            Long userId,
            BigDecimal longitude,
            BigDecimal latitude,
            List<String> interests,
            String zodiac,
            String chineseZodiac,
            Integer age,
            Integer gender,
            List<Map<String, Object>> targetUsers) {
        
        List<Map<String, Object>> matchResults = new ArrayList<>();
        
        for (Map<String, Object> targetUser : targetUsers) {
            Long targetUserId = (Long) targetUser.get("userId");
            if (targetUserId.equals(userId)) continue; // 跳过自己
            
            // 1. 距离分 (30%)
            BigDecimal targetLng = (BigDecimal) targetUser.get("longitude");
            BigDecimal targetLat = (BigDecimal) targetUser.get("latitude");
            double distanceScore = calculateDistanceScore(longitude, latitude, targetLng, targetLat);
            
            // 2. 兴趣分 (25%)
            @SuppressWarnings("unchecked")
            List<String> targetInterests = (List<String>) targetUser.get("interests");
            double interestScore = calculateInterestScore(interests, targetInterests);
            
            // 3. 星座分 (15%)
            String targetZodiac = (String) targetUser.get("zodiac");
            double zodiacScore = calculateZodiacScore(zodiac, targetZodiac);
            
            // 4. 年龄分 (15%)
            Integer targetAge = (Integer) targetUser.get("age");
            double ageScore = calculateAgeScore(age, targetAge);
            
            // 5. 活动偏好分 (10%) - 简化处理
            double activityScore = 50.0;
            
            // 6. 行为分 (5%) - 简化处理
            double behaviorScore = 50.0;
            
            // 计算总分
            double totalScore = distanceScore * 0.30 +
                               interestScore * 0.25 +
                               zodiacScore * 0.15 +
                               ageScore * 0.15 +
                               activityScore * 0.10 +
                               behaviorScore * 0.05;
            
            Map<String, Object> matchResult = new HashMap<>();
            matchResult.put("userId", targetUserId);
            matchResult.put("nickname", targetUser.get("nickname"));
            matchResult.put("avatar", targetUser.get("avatar"));
            matchResult.put("age", targetAge);
            matchResult.put("gender", targetUser.get("gender"));
            matchResult.put("distance", calculateDistance(longitude, latitude, targetLng, targetLat));
            matchResult.put("commonInterests", getCommonInterests(interests, targetInterests));
            matchResult.put("totalScore", Math.round(totalScore));
            matchResult.put("isMatch", totalScore >= 60);
            matchResult.put("isHighMatch", totalScore >= 80);
            
            matchResults.add(matchResult);
        }
        
        // 按分数排序
        matchResults.sort((a, b) -> ((Integer) b.get("totalScore")).compareTo((Integer) a.get("totalScore")));
        
        return Result.success(matchResults);
    }
    
    /**
     * 计算距离分数
     */
    private double calculateDistanceScore(BigDecimal lng1, BigDecimal lat1, BigDecimal lng2, BigDecimal lat2) {
        double distance = calculateDistance(lng1, lat1, lng2, lat2);
        // 10km内满分，超过10km为0
        return Math.max(0, 100 - distance * 10);
    }
    
    /**
     * 计算两点距离（km）
     */
    private double calculateDistance(BigDecimal lng1, BigDecimal lat1, BigDecimal lng2, BigDecimal lat2) {
        if (lng1 == null || lat1 == null || lng2 == null || lat2 == null) {
            return 9999;
        }
        
        double radLat1 = Math.toRadians(lat1.doubleValue());
        double radLat2 = Math.toRadians(lat2.doubleValue());
        double a = radLat1 - radLat2;
        double b = Math.toRadians(lng1.doubleValue()) - Math.toRadians(lng2.doubleValue());
        
        double s = 2 * Math.asin(Math.sqrt(Math.pow(Math.sin(a / 2), 2) +
                Math.cos(radLat1) * Math.cos(radLat2) * Math.pow(Math.sin(b / 2), 2)));
        s = s * 6371; // 地球半径6371km
        return Math.round(s * 100) / 100.0;
    }
    
    /**
     * 计算兴趣匹配分数
     */
    private double calculateInterestScore(List<String> interests, List<String> targetInterests) {
        if (interests == null || targetInterests == null || interests.isEmpty() || targetInterests.isEmpty()) {
            return 0;
        }
        
        long commonCount = interests.stream()
                .filter(targetInterests::contains)
                .count();
        
        // 共同兴趣越多分越高
        double score = (double) commonCount / Math.max(interests.size(), targetInterests.size()) * 100;
        return Math.min(100, score + commonCount * 10); //  bonus for each common interest
    }
    
    /**
     * 获取共同兴趣
     */
    private List<String> getCommonInterests(List<String> interests, List<String> targetInterests) {
        if (interests == null || targetInterests == null) {
            return new ArrayList<>();
        }
        List<String> common = new ArrayList<>(interests);
        common.retainAll(targetInterests);
        return common;
    }
    
    /**
     * 计算星座匹配分数
     */
    private double calculateZodiacScore(String zodiac, String targetZodiac) {
        if (zodiac == null || targetZodiac == null) {
            return 50;
        }
        
        Map<String, Integer> compatibility = ZODIAC_COMPATIBILITY.get(zodiac);
        if (compatibility == null) {
            return 50;
        }
        
        return compatibility.getOrDefault(targetZodiac, 50);
    }
    
    /**
     * 计算年龄匹配分数
     */
    private double calculateAgeScore(Integer age, Integer targetAge) {
        if (age == null || targetAge == null) {
            return 50;
        }
        
        int diff = Math.abs(age - targetAge);
        return Math.max(0, 100 - diff * 5);
    }
    
    /**
     * 获取附近的人
     */
    public Result<List<Map<String, Object>>> getNearbyUsers(
            BigDecimal longitude,
            BigDecimal latitude,
            Double radiusKm) {
        
        // 简化实现：实际应该使用Redis Geo或Elasticsearch
        // 这里返回模拟数据
        log.info("获取附近的人: lng={}, lat={}, radius={}km", longitude, latitude, radiusKm);
        
        return Result.success(new ArrayList<>());
    }
}
