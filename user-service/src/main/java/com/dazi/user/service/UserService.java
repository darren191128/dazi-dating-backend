package com.dazi.user.service;

import com.dazi.common.result.Result;
import com.dazi.user.entity.*;
import com.dazi.user.repository.*;
import com.dazi.common.util.JwtUtil;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Slf4j
@Service
@RequiredArgsConstructor
public class UserService {
    
    private final UserRepository userRepository;
    private final UserProfileRepository userProfileRepository;
    private final UserFollowRepository userFollowRepository;
    private final UserVisitorRepository userVisitorRepository;
    private final UserMatchRepository userMatchRepository;
    private final JwtUtil jwtUtil;
    
    /**
     * 微信登录
     */
    @Transactional
    public Result<Map<String, Object>> wxLogin(String openId, String nickname, String avatar, Integer gender) {
        // 查询用户是否存在
        User user = userRepository.findByOpenId(openId);
        
        if (user == null) {
            // 新用户注册
            user = new User();
            user.setOpenId(openId);
            user.setNickname(nickname);
            user.setAvatar(avatar);
            user.setGender(gender);
            user.setLevel(1);
            user.setExp(0);
            user.setCreditScore(100);
            user.setStatus(1);
            user.setLastLoginTime(LocalDateTime.now());
            userRepository.insert(user);
            
            // 创建用户资料
            UserProfile profile = new UserProfile();
            profile.setUserId(user.getId());
            userProfileRepository.insert(profile);
            
            log.info("新用户注册: userId={}, openId={}", user.getId(), openId);
        } else {
            // 更新登录时间
            user.setLastLoginTime(LocalDateTime.now());
            userRepository.updateById(user);
            log.info("用户登录: userId={}", user.getId());
        }
        
        // 生成JWT token
        String token = jwtUtil.generateToken(user.getId(), user.getOpenId());
        
        Map<String, Object> result = new HashMap<>();
        result.put("token", token);
        result.put("userId", user.getId());
        result.put("isNewUser", user.getLevel() == 1 && user.getExp() == 0);
        
        return Result.success(result);
    }
    
    /**
     * 获取用户信息
     */
    public Result<Map<String, Object>> getUserInfo(Long userId) {
        User user = userRepository.selectById(userId);
        if (user == null) {
            return Result.error("用户不存在");
        }
        
        UserProfile profile = userProfileRepository.findByUserId(userId);
        
        Map<String, Object> result = new HashMap<>();
        result.put("userId", user.getId());
        result.put("nickname", user.getNickname());
        result.put("avatar", user.getAvatar());
        result.put("gender", user.getGender());
        result.put("age", user.getAge());
        result.put("level", user.getLevel());
        result.put("exp", user.getExp());
        result.put("creditScore", user.getCreditScore());
        result.put("bio", profile != null ? profile.getBio() : "");
        result.put("zodiac", profile != null ? profile.getZodiac() : "");
        result.put("chineseZodiac", profile != null ? profile.getChineseZodiac() : "");
        result.put("realNameVerified", profile != null ? profile.getRealNameVerified() : 0);
        
        // 添加关注统计
        result.put("followingCount", userFollowRepository.countFollowing(userId));
        result.put("followerCount", userFollowRepository.countFollowers(userId));
        
        return Result.success(result);
    }
    
    /**
     * 更新用户资料
     */
    @Transactional
    public Result<Void> updateProfile(Long userId, String nickname, String bio, Integer age) {
        User user = userRepository.selectById(userId);
        if (user == null) {
            return Result.error("用户不存在");
        }
        
        if (nickname != null) {
            user.setNickname(nickname);
        }
        if (age != null) {
            user.setAge(age);
        }
        userRepository.updateById(user);
        
        if (bio != null) {
            UserProfile profile = userProfileRepository.findByUserId(userId);
            if (profile != null) {
                profile.setBio(bio);
                userProfileRepository.updateById(profile);
            }
        }
        
        return Result.success();
    }
    
    /**
     * 增加经验值
     */
    @Transactional
    public void addExp(Long userId, Integer exp) {
        User user = userRepository.selectById(userId);
        if (user == null) return;
        
        int newExp = user.getExp() + exp;
        int newLevel = calculateLevel(newExp);
        
        user.setExp(newExp);
        user.setLevel(newLevel);
        userRepository.updateById(user);
        
        log.info("用户{}增加经验值{}，当前等级{}，总经验值{}", userId, exp, newLevel, newExp);
    }
    
    /**
     * 计算等级
     */
    private int calculateLevel(int exp) {
        if (exp >= 3000) return 8;
        if (exp >= 2200) return 7;
        if (exp >= 1500) return 6;
        if (exp >= 1000) return 5;
        if (exp >= 600) return 4;
        if (exp >= 300) return 3;
        if (exp >= 100) return 2;
        return 1;
    }
    
    // ==================== 关注相关方法 ====================
    
    /**
     * 关注用户
     */
    @Transactional
    public Result<Void> followUser(Long userId, Long followUserId) {
        if (userId.equals(followUserId)) {
            return Result.error("不能关注自己");
        }
        
        // 检查目标用户是否存在
        User targetUser = userRepository.selectById(followUserId);
        if (targetUser == null) {
            return Result.error("用户不存在");
        }
        
        // 检查是否已经关注
        Integer exists = userFollowRepository.existsByUserIdAndFollowUserId(userId, followUserId);
        if (exists != null && exists > 0) {
            return Result.error("已经关注过了");
        }
        
        UserFollow follow = new UserFollow();
        follow.setUserId(userId);
        follow.setFollowUserId(followUserId);
        userFollowRepository.insert(follow);
        
        // 记录访客（关注也算一种访问）
        recordVisit(followUserId, userId);
        
        log.info("用户关注: userId={}, followUserId={}", userId, followUserId);
        
        return Result.success();
    }
    
    /**
     * 取消关注
     */
    @Transactional
    public Result<Void> unfollowUser(Long userId, Long followUserId) {
        UserFollow follow = userFollowRepository.findByUserIdAndFollowUserId(userId, followUserId);
        if (follow == null) {
            return Result.error("尚未关注");
        }
        
        userFollowRepository.deleteById(follow.getId());
        
        log.info("用户取消关注: userId={}, followUserId={}", userId, followUserId);
        
        return Result.success();
    }
    
    /**
     * 获取关注列表
     */
    public Result<List<Map<String, Object>>> getFollowings(Long userId, Integer page, Integer pageSize) {
        if (page == null || page < 1) page = 1;
        if (pageSize == null || pageSize < 1 || pageSize > 100) pageSize = 20;
        
        int offset = (page - 1) * pageSize;
        List<UserFollow> follows = userFollowRepository.findFollowingByUserId(userId, offset, pageSize);
        
        List<Map<String, Object>> result = new ArrayList<>();
        for (UserFollow follow : follows) {
            Map<String, Object> map = getUserBasicInfo(follow.getFollowUserId());
            if (map != null) {
                map.put("followTime", follow.getCreatedAt());
                result.add(map);
            }
        }
        
        return Result.success(result);
    }
    
    /**
     * 获取粉丝列表
     */
    public Result<List<Map<String, Object>>> getFollowers(Long userId, Integer page, Integer pageSize) {
        if (page == null || page < 1) page = 1;
        if (pageSize == null || pageSize < 1 || pageSize > 100) pageSize = 20;
        
        int offset = (page - 1) * pageSize;
        List<UserFollow> follows = userFollowRepository.findFollowersByUserId(userId, offset, pageSize);
        
        List<Map<String, Object>> result = new ArrayList<>();
        for (UserFollow follow : follows) {
            Map<String, Object> map = getUserBasicInfo(follow.getUserId());
            if (map != null) {
                map.put("followTime", follow.getCreatedAt());
                // 检查是否互相关注
                Integer isMutual = userFollowRepository.existsByUserIdAndFollowUserId(userId, follow.getUserId());
                map.put("isMutual", isMutual != null && isMutual > 0);
                result.add(map);
            }
        }
        
        return Result.success(result);
    }
    
    /**
     * 获取访客记录
     */
    public Result<List<Map<String, Object>>> getVisitors(Long userId, Integer page, Integer pageSize) {
        if (page == null || page < 1) page = 1;
        if (pageSize == null || pageSize < 1 || pageSize > 100) pageSize = 20;
        
        int offset = (page - 1) * pageSize;
        List<UserVisitor> visitors = userVisitorRepository.findByUserIdWithPagination(userId, offset, pageSize);
        
        List<Map<String, Object>> result = new ArrayList<>();
        for (UserVisitor visitor : visitors) {
            Map<String, Object> map = getUserBasicInfo(visitor.getVisitorId());
            if (map != null) {
                map.put("visitCount", visitor.getVisitCount());
                map.put("lastVisitAt", visitor.getLastVisitAt());
                result.add(map);
            }
        }
        
        return Result.success(result);
    }
    
    /**
     * 记录访客
     */
    @Transactional
    public void recordVisit(Long userId, Long visitorId) {
        if (userId.equals(visitorId)) {
            return;
        }
        
        UserVisitor visitor = userVisitorRepository.findByUserIdAndVisitorId(userId, visitorId);
        if (visitor == null) {
            visitor = new UserVisitor();
            visitor.setUserId(userId);
            visitor.setVisitorId(visitorId);
            visitor.setVisitCount(1);
            visitor.setLastVisitAt(LocalDateTime.now());
            userVisitorRepository.insert(visitor);
        } else {
            userVisitorRepository.incrementVisitCount(visitor.getId());
        }
    }
    
    // ==================== 匹配相关方法 ====================
    
    /**
     * 喜欢用户（滑动匹配）
     */
    @Transactional
    public Result<Map<String, Object>> matchLike(Long userId, Long targetUserId) {
        if (userId.equals(targetUserId)) {
            return Result.error("不能喜欢自己");
        }
        
        // 检查目标用户是否存在
        User targetUser = userRepository.selectById(targetUserId);
        if (targetUser == null) {
            return Result.error("用户不存在");
        }
        
        // 检查是否已经操作过
        UserMatch existing = userMatchRepository.findByUserIdAndTargetUserId(userId, targetUserId);
        if (existing != null) {
            return Result.error("已经操作过了");
        }
        
        // 检查对方是否喜欢自己
        UserMatch reverseMatch = userMatchRepository.findReverseLike(userId, targetUserId);
        boolean isMutual = reverseMatch != null;
        
        // 保存匹配记录
        UserMatch match = new UserMatch();
        match.setUserId(userId);
        match.setTargetUserId(targetUserId);
        match.setAction(1); // 喜欢
        match.setIsMutual(isMutual ? 1 : 0);
        userMatchRepository.insert(match);
        
        // 如果对方也喜欢自己，更新对方的记录
        if (isMutual) {
            userMatchRepository.updateMutual(targetUserId, userId);
            log.info("互相喜欢: userId={}, targetUserId={}", userId, targetUserId);
        }
        
        // 记录访客
        recordVisit(targetUserId, userId);
        
        log.info("用户喜欢: userId={}, targetUserId={}, isMutual={}", userId, targetUserId, isMutual);
        
        Map<String, Object> result = new HashMap<>();
        result.put("isMutual", isMutual);
        result.put("matchId", match.getId());
        
        return Result.success(result);
    }
    
    /**
     * 不喜欢用户
     */
    @Transactional
    public Result<Void> matchDislike(Long userId, Long targetUserId) {
        if (userId.equals(targetUserId)) {
            return Result.error("不能对自己操作");
        }
        
        // 检查目标用户是否存在
        User targetUser = userRepository.selectById(targetUserId);
        if (targetUser == null) {
            return Result.error("用户不存在");
        }
        
        // 检查是否已经操作过
        UserMatch existing = userMatchRepository.findByUserIdAndTargetUserId(userId, targetUserId);
        if (existing != null) {
            return Result.error("已经操作过了");
        }
        
        UserMatch match = new UserMatch();
        match.setUserId(userId);
        match.setTargetUserId(targetUserId);
        match.setAction(2); // 不喜欢
        match.setIsMutual(0);
        userMatchRepository.insert(match);
        
        log.info("用户不喜欢: userId={}, targetUserId={}", userId, targetUserId);
        
        return Result.success();
    }
    
    /**
     * 获取互相喜欢列表
     */
    public Result<List<Map<String, Object>>> getMutualMatches(Long userId, Integer page, Integer pageSize) {
        if (page == null || page < 1) page = 1;
        if (pageSize == null || pageSize < 1 || pageSize > 100) pageSize = 20;
        
        int offset = (page - 1) * pageSize;
        List<UserMatch> matches = userMatchRepository.findMutualMatches(userId, offset, pageSize);
        
        List<Map<String, Object>> result = new ArrayList<>();
        for (UserMatch match : matches) {
            Map<String, Object> map = getUserBasicInfo(match.getTargetUserId());
            if (map != null) {
                map.put("matchId", match.getId());
                map.put("matchTime", match.getCreatedAt());
                result.add(map);
            }
        }
        
        return Result.success(result);
    }
    
    /**
     * 获取用户基本信息
     */
    private Map<String, Object> getUserBasicInfo(Long userId) {
        User user = userRepository.selectById(userId);
        if (user == null) {
            return null;
        }
        
        Map<String, Object> map = new HashMap<>();
        map.put("userId", user.getId());
        map.put("nickname", user.getNickname());
        map.put("avatar", user.getAvatar());
        map.put("gender", user.getGender());
        map.put("age", user.getAge());
        return map;
    }
}
