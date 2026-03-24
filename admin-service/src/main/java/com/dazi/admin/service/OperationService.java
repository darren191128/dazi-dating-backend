package com.dazi.admin.service;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.dazi.activity.entity.Activity;
import com.dazi.activity.repository.ActivityRepository;
import com.dazi.common.result.Result;
import com.dazi.user.entity.User;
import com.dazi.user.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.TimeUnit;

/**
 * 运营管理服务
 */
@Slf4j
@Service
@RequiredArgsConstructor
public class OperationService {
    
    private final UserRepository userRepository;
    private final ActivityRepository activityRepository;
    private final RedisTemplate<String, Object> redisTemplate;
    
    private static final String OPERATION_CACHE_PREFIX = "operation:cache:";
    
    // ==================== 会员管理 ====================
    
    /**
     * 获取会员列表
     */
    public Result<Page<User>> getUserList(Integer page, Integer size, String keyword, 
                                          Integer status, Integer gender) {
        Page<User> pageParam = new Page<>(page, size);
        
        com.baomidou.mybatisplus.core.conditions.query.QueryWrapper<User> wrapper = 
            new com.baomidou.mybatisplus.core.conditions.query.QueryWrapper<>();
        
        if (keyword != null && !keyword.isEmpty()) {
            wrapper.like("nickname", keyword)
                   .or()
                   .like("phone", keyword);
        }
        if (status != null) {
            wrapper.eq("status", status);
        }
        if (gender != null) {
            wrapper.eq("gender", gender);
        }
        
        wrapper.orderByDesc("create_time");
        
        Page<User> result = userRepository.selectPage(pageParam, wrapper);
        
        // 不返回敏感信息
        result.getRecords().forEach(user -> {
            user.setOpenId(null);
            user.setUnionId(null);
        });
        
        return Result.success(result);
    }
    
    /**
     * 获取会员详情
     */
    public Result<User> getUserDetail(Long userId) {
        User user = userRepository.selectById(userId);
        if (user != null) {
            user.setOpenId(null);
            user.setUnionId(null);
        }
        return Result.success(user);
    }
    
    /**
     * 编辑会员
     */
    @Transactional
    public Result<Void> updateUser(Long userId, User user, Long operatorId) {
        User existing = userRepository.selectById(userId);
        if (existing == null) {
            return Result.error("用户不存在");
        }
        
        user.setId(userId);
        // 不允许修改的字段
        user.setOpenId(null);
        user.setUnionId(null);
        user.setPassword(null);
        user.setCreateTime(null);
        
        userRepository.updateById(user);
        
        log.info("编辑会员: operatorId={}, userId={}", operatorId, userId);
        
        // 清除缓存
        clearUserCache(userId);
        
        return Result.success();
    }
    
    /**
     * 删除会员（软删除）
     */
    @Transactional
    public Result<Void> deleteUser(Long userId, Long operatorId) {
        User user = userRepository.selectById(userId);
        if (user == null) {
            return Result.error("用户不存在");
        }
        
        userRepository.deleteById(userId);
        
        log.info("删除会员: operatorId={}, userId={}", operatorId, userId);
        
        clearUserCache(userId);
        
        return Result.success();
    }
    
    /**
     * 禁用/启用会员
     */
    @Transactional
    public Result<Void> toggleUserStatus(Long userId, Integer status, Long operatorId) {
        User user = userRepository.selectById(userId);
        if (user == null) {
            return Result.error("用户不存在");
        }
        
        user.setStatus(status);
        userRepository.updateById(user);
        
        log.info("{}会员: operatorId={}, userId={}", 
                status == 1 ? "启用" : "禁用", operatorId, userId);
        
        clearUserCache(userId);
        
        return Result.success();
    }
    
    // ==================== 活动管理 ====================
    
    /**
     * 获取活动列表
     */
    public Result<Page<Activity>> getActivityList(Integer page, Integer size, String keyword,
                                                  Integer status, Integer type) {
        Page<Activity> pageParam = new Page<>(page, size);
        
        com.baomidou.mybatisplus.core.conditions.query.QueryWrapper<Activity> wrapper = 
            new com.baomidou.mybatisplus.core.conditions.query.QueryWrapper<>();
        
        if (keyword != null && !keyword.isEmpty()) {
            wrapper.like("title", keyword);
        }
        if (status != null) {
            wrapper.eq("status", status);
        }
        if (type != null) {
            wrapper.eq("type", type);
        }
        
        wrapper.orderByDesc("create_time");
        
        Page<Activity> result = activityRepository.selectPage(pageParam, wrapper);
        
        return Result.success(result);
    }
    
    /**
     * 获取活动详情
     */
    public Result<Activity> getActivityDetail(Long activityId) {
        Activity activity = activityRepository.selectById(activityId);
        return Result.success(activity);
    }
    
    /**
     * 编辑活动
     */
    @Transactional
    public Result<Void> updateActivity(Long activityId, Activity activity, Long operatorId) {
        Activity existing = activityRepository.selectById(activityId);
        if (existing == null) {
            return Result.error("活动不存在");
        }
        
        activity.setId(activityId);
        // 不允许修改的字段
        activity.setUserId(null);
        activity.setCurrentParticipants(null);
        activity.setCreateTime(null);
        
        activityRepository.updateById(activity);
        
        log.info("编辑活动: operatorId={}, activityId={}", operatorId, activityId);
        
        clearActivityCache(activityId);
        
        return Result.success();
    }
    
    /**
     * 删除活动
     */
    @Transactional
    public Result<Void> deleteActivity(Long activityId, Long operatorId) {
        Activity activity = activityRepository.selectById(activityId);
        if (activity == null) {
            return Result.error("活动不存在");
        }
        
        activityRepository.deleteById(activityId);
        
        log.info("删除活动: operatorId={}, activityId={}", operatorId, activityId);
        
        clearActivityCache(activityId);
        
        return Result.success();
    }
    
    /**
     * 审核活动
     */
    @Transactional
    public Result<Void> auditActivity(Long activityId, Integer status, String reason, Long operatorId) {
        Activity activity = activityRepository.selectById(activityId);
        if (activity == null) {
            return Result.error("活动不存在");
        }
        
        activity.setStatus(status);
        activityRepository.updateById(activity);
        
        log.info("审核活动: operatorId={}, activityId={}, status={}, reason={}", 
                operatorId, activityId, status, reason);
        
        clearActivityCache(activityId);
        
        return Result.success();
    }
    
    // ==================== 数据统计 ====================
    
    /**
     * 获取运营数据概览
     */
    public Result<Map<String, Object>> getDashboardData() {
        String cacheKey = OPERATION_CACHE_PREFIX + "dashboard";
        
        @SuppressWarnings("unchecked")
        Map<String, Object> cached = (Map<String, Object>) redisTemplate.opsForValue().get(cacheKey);
        if (cached != null) {
            return Result.success(cached);
        }
        
        // 统计用户数据
        Long totalUsers = userRepository.selectCount(null);
        Long todayNewUsers = userRepository.selectCount(
            new com.baomidou.mybatisplus.core.conditions.query.QueryWrapper<User>()
                .ge("create_time", LocalDateTime.now().toLocalDate().atStartOfDay())
        );
        
        // 统计活动数据
        Long totalActivities = activityRepository.selectCount(null);
        Long ongoingActivities = activityRepository.selectCount(
            new com.baomidou.mybatisplus.core.conditions.query.QueryWrapper<Activity>()
                .eq("status", 1)
        );
        
        Map<String, Object> data = new HashMap<>();
        data.put("totalUsers", totalUsers);
        data.put("todayNewUsers", todayNewUsers);
        data.put("totalActivities", totalActivities);
        data.put("ongoingActivities", ongoingActivities);
        
        // 缓存5分钟
        redisTemplate.opsForValue().set(cacheKey, data, 5, TimeUnit.MINUTES);
        
        return Result.success(data);
    }
    
    // ==================== 私有方法 ====================
    
    private void clearUserCache(Long userId) {
        // 清除用户相关缓存
        redisTemplate.delete(OPERATION_CACHE_PREFIX + "user:" + userId);
        redisTemplate.delete(OPERATION_CACHE_PREFIX + "dashboard");
    }
    
    private void clearActivityCache(Long activityId) {
        // 清除活动相关缓存
        redisTemplate.delete(OPERATION_CACHE_PREFIX + "activity:" + activityId);
        redisTemplate.delete(OPERATION_CACHE_PREFIX + "dashboard");
    }
}
