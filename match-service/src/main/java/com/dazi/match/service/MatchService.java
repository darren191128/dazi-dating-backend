package com.dazi.match.service;

import com.dazi.common.result.Result;
import com.dazi.match.entity.UserLocation;
import com.dazi.match.repository.UserLocationRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.geo.Circle;
import org.springframework.data.geo.Distance;
import org.springframework.data.geo.GeoResults;
import org.springframework.data.geo.Metrics;
import org.springframework.data.geo.Point;
import org.springframework.data.redis.connection.RedisGeoCommands;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.time.LocalDateTime;
import java.time.temporal.ChronoUnit;
import java.util.*;
import java.util.stream.Collectors;

@Slf4j
@Service
@RequiredArgsConstructor
public class MatchService {
    
    private final RedisTemplate<String, Object> redisTemplate;
    private final UserLocationRepository userLocationRepository;
    
    private static final String GEO_KEY = "user:location";
    private static final String DAILY_PICKS_KEY = "daily:picks:";
    
    // 星座相容性评分表
    private static final Map<String, Map<String, Integer>> ZODIAC_COMPATIBILITY = new HashMap<>();
    
    static {
        ZODIAC_COMPATIBILITY.put("白羊座", Map.of("狮子座", 100, "射手座", 100, "双子座", 80, "水瓶座", 70));
        ZODIAC_COMPATIBILITY.put("金牛座", Map.of("处女座", 100, "摩羯座", 100, "巨蟹座", 80, "双鱼座", 70));
        ZODIAC_COMPATIBILITY.put("双子座", Map.of("天秤座", 100, "水瓶座", 100, "白羊座", 80, "狮子座", 70));
        ZODIAC_COMPATIBILITY.put("巨蟹座", Map.of("天蝎座", 100, "双鱼座", 100, "金牛座", 80, "处女座", 70));
        ZODIAC_COMPATIBILITY.put("狮子座", Map.of("白羊座", 100, "射手座", 100, "双子座", 80, "天秤座", 70));
        ZODIAC_COMPATIBILITY.put("处女座", Map.of("金牛座", 100, "摩羯座", 100, "巨蟹座", 80, "天蝎座", 70));
        ZODIAC_COMPATIBILITY.put("天秤座", Map.of("双子座", 100, "水瓶座", 100, "狮子座", 80, "射手座", 70));
        ZODIAC_COMPATIBILITY.put("天蝎座", Map.of("巨蟹座", 100, "双鱼座", 100, "处女座", 80, "摩羯座", 70));
        ZODIAC_COMPATIBILITY.put("射手座", Map.of("白羊座", 100, "狮子座", 100, "天秤座", 80, "水瓶座", 70));
        ZODIAC_COMPATIBILITY.put("摩羯座", Map.of("金牛座", 100, "处女座", 100, "天蝎座", 80, "双鱼座", 70));
        ZODIAC_COMPATIBILITY.put("水瓶座", Map.of("双子座", 100, "天秤座", 100, "白羊座", 80, "射手座", 70));
        ZODIAC_COMPATIBILITY.put("双鱼座", Map.of("巨蟹座", 100, "天蝎座", 100, "金牛座", 80, "摩羯座", 70));
    }
    
    /**
     * 获取推荐匹配（支持高级筛选）
     */
    public Result<List<Map<String, Object>>> getRecommendations(
            Long userId, Integer minHeight, Integer maxHeight, String education,
            String occupation, Integer minIncome, Integer maxIncome, 
            Boolean hasHouse, Boolean hasCar, String maritalStatus,
            Integer page, Integer pageSize) {
        
        // 获取当前用户信息
        UserLocation userLocation = userLocationRepository.findByUserId(userId);
        if (userLocation == null) {
            return Result.error("请先设置位置信息");
        }
        
        // 从数据库获取候选用户（带筛选条件）
        List<UserLocation> candidates = userLocationRepository.findCandidatesWithFilter(
                userId, minHeight, maxHeight, education, occupation, 
                minIncome, maxIncome, hasHouse, hasCar, maritalStatus,
                (page - 1) * pageSize, pageSize);
        
        List<Map<String, Object>> matchResults = new ArrayList<>();
        
        for (UserLocation candidate : candidates) {
            if (candidate.getUserId().equals(userId)) continue;
            
            // 计算各项分数
            double distanceScore = calculateDistanceScore(
                userLocation.getLongitude(), userLocation.getLatitude(),
                candidate.getLongitude(), candidate.getLatitude()
            );
            
            double interestScore = calculateInterestScore(
                userLocation.getInterests(), candidate.getInterests()
            );
            
            double zodiacScore = calculateZodiacScore(
                userLocation.getZodiac(), candidate.getZodiac()
            );
            
            double ageScore = calculateAgeScore(
                userLocation.getAge(), candidate.getAge()
            );
            
            // 计算总分
            double totalScore = distanceScore * 0.30 +
                               interestScore * 0.25 +
                               zodiacScore * 0.15 +
                               ageScore * 0.15 +
                               50 * 0.10 + // 活动偏好分
                               50 * 0.05;  // 行为分
            
            Map<String, Object> matchResult = new HashMap<>();
            matchResult.put("userId", candidate.getUserId());
            matchResult.put("nickname", candidate.getNickname());
            matchResult.put("avatar", candidate.getAvatar());
            matchResult.put("age", candidate.getAge());
            matchResult.put("gender", candidate.getGender());
            matchResult.put("distance", calculateDistance(
                userLocation.getLongitude(), userLocation.getLatitude(),
                candidate.getLongitude(), candidate.getLatitude()
            ));
            matchResult.put("commonInterests", getCommonInterests(
                userLocation.getInterests(), candidate.getInterests()
            ));
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
     * 获取每日精选
     */
    public Result<List<Map<String, Object>>> getDailyPicks(Long userId, Integer limit) {
        String cacheKey = DAILY_PICKS_KEY + userId + ":" + LocalDateTime.now().getDayOfYear();
        
        // 尝试从缓存获取
        @SuppressWarnings("unchecked")
        List<Map<String, Object>> cached = (List<Map<String, Object>>) redisTemplate.opsForValue().get(cacheKey);
        if (cached != null) {
            return Result.success(cached);
        }
        
        // 获取当前用户信息
        UserLocation userLocation = userLocationRepository.findByUserId(userId);
        if (userLocation == null) {
            return Result.error("请先设置位置信息");
        }
        
        // 获取高质量候选用户
        List<UserLocation> candidates = userLocationRepository.findHighQualityUsers(userId, limit * 3);
        
        List<Map<String, Object>> picks = new ArrayList<>();
        
        for (UserLocation candidate : candidates) {
            if (candidate.getUserId().equals(userId)) continue;
            
            // 计算匹配度分数
            double matchScore = calculateMatchScore(userLocation, candidate);
            
            Map<String, Object> pick = new HashMap<>();
            pick.put("userId", candidate.getUserId());
            pick.put("nickname", candidate.getNickname());
            pick.put("avatar", candidate.getAvatar());
            pick.put("age", candidate.getAge());
            pick.put("gender", candidate.getGender());
            pick.put("bio", candidate.getBio());
            pick.put("distance", calculateDistance(
                userLocation.getLongitude(), userLocation.getLatitude(),
                candidate.getLongitude(), candidate.getLatitude()
            ));
            pick.put("matchScore", Math.round(matchScore));
            pick.put("matchPercent", Math.min(100, (int) matchScore));
            
            picks.add(pick);
        }
        
        // 按匹配度排序并限制数量
        picks.sort((a, b) -> ((Integer) b.get("matchScore")).compareTo((Integer) a.get("matchScore")));
        List<Map<String, Object>> result = picks.stream().limit(limit).collect(Collectors.toList());
        
        // 缓存结果（1小时）
        redisTemplate.opsForValue().set(cacheKey, result, 1, java.util.concurrent.TimeUnit.HOURS);
        
        return Result.success(result);
    }
    
    /**
     * 计算匹配分数
     */
    private double calculateMatchScore(UserLocation user, UserLocation candidate) {
        double distanceScore = calculateDistanceScore(
            user.getLongitude(), user.getLatitude(),
            candidate.getLongitude(), candidate.getLatitude()
        );
        
        double interestScore = calculateInterestScore(
            user.getInterests(), candidate.getInterests()
        );
        
        double zodiacScore = calculateZodiacScore(
            user.getZodiac(), candidate.getZodiac()
        );
        
        double ageScore = calculateAgeScore(
            user.getAge(), candidate.getAge()
        );
        
        return distanceScore * 0.30 +
               interestScore * 0.25 +
               zodiacScore * 0.20 +
               ageScore * 0.15 +
               50 * 0.10;
    }
    
    /**
     * 获取附近的人（使用Redis Geo）
     */
    public Result<List<Map<String, Object>>> getNearbyUsers(
            Long userId, BigDecimal longitude, BigDecimal latitude, 
            Double radiusKm, Integer page, Integer pageSize) {
        
        // 更新用户位置到Redis
        updateUserLocation(userId, longitude, latitude);
        
        // 使用Redis Geo查询附近用户
        Point center = new Point(longitude.doubleValue(), latitude.doubleValue());
        Distance radius = new Distance(radiusKm, Metrics.KILOMETERS);
        Circle circle = new Circle(center, radius);
        
        RedisGeoCommands.GeoRadiusCommandArgs args = RedisGeoCommands.GeoRadiusCommandArgs
                .newGeoRadiusArgs()
                .includeDistance()
                .includeCoordinates()
                .sortAscending()
                .limit(pageSize);
        
        GeoResults<RedisGeoCommands.GeoLocation<Object>> results = 
                redisTemplate.opsForGeo().radius(GEO_KEY, circle, args);
        
        List<Map<String, Object>> nearbyUsers = new ArrayList<>();
        
        if (results != null) {
            results.forEach(result -> {
                Long targetUserId = Long.valueOf(result.getContent().getName().toString());
                if (!targetUserId.equals(userId)) {
                    Map<String, Object> user = new HashMap<>();
                    user.put("userId", targetUserId);
                    user.put("distance", result.getDistance().getValue());
                    // 从数据库获取用户详细信息
                    UserLocation location = userLocationRepository.findByUserId(targetUserId);
                    if (location != null) {
                        user.put("nickname", location.getNickname());
                        user.put("avatar", location.getAvatar());
                        user.put("age", location.getAge());
                        user.put("gender", location.getGender());
                    }
                    nearbyUsers.add(user);
                }
            });
        }
        
        return Result.success(nearbyUsers);
    }
    
    /**
     * 获取用户详情
     */
    public Result<Map<String, Object>> getUserDetail(Long userId, Long targetUserId) {
        UserLocation location = userLocationRepository.findByUserId(targetUserId);
        if (location == null) {
            return Result.error("用户不存在");
        }
        
        Map<String, Object> detail = new HashMap<>();
        detail.put("userId", location.getUserId());
        detail.put("nickname", location.getNickname());
        detail.put("avatar", location.getAvatar());
        detail.put("age", location.getAge());
        detail.put("gender", location.getGender());
        detail.put("bio", location.getBio());
        detail.put("zodiac", location.getZodiac());
        detail.put("chineseZodiac", location.getChineseZodiac());
        detail.put("interests", location.getInterests());
        
        return Result.success(detail);
    }
    
    /**
     * 更新用户位置
     */
    private void updateUserLocation(Long userId, BigDecimal longitude, BigDecimal latitude) {
        redisTemplate.opsForGeo().add(GEO_KEY, 
            new Point(longitude.doubleValue(), latitude.doubleValue()), 
            userId.toString());
    }
    
    // 辅助计算方法...
    private double calculateDistanceScore(BigDecimal lng1, BigDecimal lat1, BigDecimal lng2, BigDecimal lat2) {
        double distance = calculateDistance(lng1, lat1, lng2, lat2);
        return Math.max(0, 100 - distance * 10);
    }
    
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
        s = s * 6371;
        return Math.round(s * 100) / 100.0;
    }
    
    private double calculateInterestScore(String interests, String targetInterests) {
        if (interests == null || targetInterests == null) {
            return 0;
        }
        
        Set<String> set1 = new HashSet<>(Arrays.asList(interests.split(",")));
        Set<String> set2 = new HashSet<>(Arrays.asList(targetInterests.split(",")));
        
        long commonCount = set1.stream().filter(set2::contains).count();
        
        if (set1.isEmpty() || set2.isEmpty()) {
            return 0;
        }
        
        double score = (double) commonCount / Math.max(set1.size(), set2.size()) * 100;
        return Math.min(100, score + commonCount * 10);
    }
    
    private List<String> getCommonInterests(String interests, String targetInterests) {
        if (interests == null || targetInterests == null) {
            return new ArrayList<>();
        }
        
        Set<String> set1 = new HashSet<>(Arrays.asList(interests.split(",")));
        Set<String> set2 = new HashSet<>(Arrays.asList(targetInterests.split(",")));
        
        List<String> common = new ArrayList<>(set1);
        common.retainAll(set2);
        return common;
    }
    
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
    
    private double calculateAgeScore(Integer age, Integer targetAge) {
        if (age == null || targetAge == null) {
            return 50;
        }
        
        int diff = Math.abs(age - targetAge);
        return Math.max(0, 100 - diff * 5);
    }
}
