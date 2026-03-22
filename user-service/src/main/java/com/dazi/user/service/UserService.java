package com.dazi.user.service;

import com.dazi.user.entity.User;
import com.dazi.user.entity.UserProfile;
import com.dazi.user.repository.UserProfileRepository;
import com.dazi.user.repository.UserRepository;
import com.dazi.common.result.Result;
import com.dazi.common.util.JwtUtil;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.Map;

@Slf4j
@Service
@RequiredArgsConstructor
public class UserService {
    
    private final UserRepository userRepository;
    private final UserProfileRepository userProfileRepository;
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
}
