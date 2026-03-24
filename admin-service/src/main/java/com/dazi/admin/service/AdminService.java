package com.dazi.admin.service;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.dazi.admin.entity.AdminUser;
import com.dazi.admin.entity.OperationLog;
import com.dazi.admin.repository.AdminUserRepository;
import com.dazi.admin.repository.OperationLogRepository;
import com.dazi.common.result.Result;
import com.dazi.common.util.JwtUtil;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.TimeUnit;

/**
 * 管理员服务
 */
@Slf4j
@Service
@RequiredArgsConstructor
public class AdminService {
    
    private final AdminUserRepository adminUserRepository;
    private final OperationLogRepository operationLogRepository;
    private final JwtUtil jwtUtil;
    private final RedisTemplate<String, Object> redisTemplate;
    private final BCryptPasswordEncoder passwordEncoder = new BCryptPasswordEncoder();
    
    private static final String ADMIN_LOGIN_ATTEMPT_PREFIX = "admin:login:attempt:";
    private static final int MAX_LOGIN_ATTEMPTS = 5;
    
    /**
     * 管理员登录
     */
    public Result<Map<String, Object>> login(String username, String password, String ip) {
        // 1. 检查登录次数限制
        String attemptKey = ADMIN_LOGIN_ATTEMPT_PREFIX + username;
        String attemptsStr = (String) redisTemplate.opsForValue().get(attemptKey);
        int attempts = attemptsStr != null ? Integer.parseInt(attemptsStr) : 0;
        
        if (attempts >= MAX_LOGIN_ATTEMPTS) {
            log.warn("管理员登录次数过多: username={}, ip={}", username, ip);
            return Result.error("登录失败次数过多，请30分钟后重试");
        }
        
        // 2. 查询管理员
        AdminUser admin = adminUserRepository.findByUsername(username);
        if (admin == null) {
            incrementLoginAttempts(attemptKey);
            return Result.error("用户名或密码错误");
        }
        
        // 3. 检查状态
        if (admin.getStatus() != 1) {
            return Result.error("账号已被禁用");
        }
        
        // 4. 验证密码
        if (!passwordEncoder.matches(password, admin.getPassword())) {
            incrementLoginAttempts(attemptKey);
            log.warn("管理员登录密码错误: username={}, ip={}", username, ip);
            return Result.error("用户名或密码错误");
        }
        
        // 5. 更新登录信息
        admin.setLastLoginTime(LocalDateTime.now());
        admin.setLastLoginIp(ip);
        adminUserRepository.updateById(admin);
        
        // 6. 生成token
        String token = jwtUtil.generateToken(admin.getId(), username);
        
        // 7. 清除登录次数记录
        redisTemplate.delete(attemptKey);
        
        // 8. 记录操作日志
        saveOperationLog(admin.getId(), admin.getRealName(), "LOGIN", "admin", "管理员登录", 
                        "username:" + username, null, ip, 1, 0L);
        
        log.info("管理员登录成功: username={}, adminId={}, ip={}", username, admin.getId(), ip);
        
        Map<String, Object> result = new HashMap<>();
        result.put("token", token);
        result.put("adminId", admin.getId());
        result.put("username", admin.getUsername());
        result.put("realName", admin.getRealName());
        result.put("role", admin.getRole());
        
        return Result.success(result);
    }
    
    /**
     * 退出登录
     */
    public Result<Void> logout(Long adminId, String token) {
        // 将token加入黑名单
        jwtUtil.blacklistToken(token);
        log.info("管理员退出登录: adminId={}", adminId);
        return Result.success();
    }
    
    /**
     * 获取管理员信息
     */
    public Result<AdminUser> getAdminInfo(Long adminId) {
        AdminUser admin = adminUserRepository.selectById(adminId);
        if (admin != null) {
            admin.setPassword(null); // 不返回密码
        }
        return Result.success(admin);
    }
    
    /**
     * 创建管理员（仅超级管理员可操作）
     */
    @Transactional
    public Result<Long> createAdmin(AdminUser admin, Long operatorId) {
        // 检查操作权限
        AdminUser operator = adminUserRepository.selectById(operatorId);
        if (operator == null || operator.getRole() != 1) {
            return Result.error("无权操作");
        }
        
        // 检查用户名是否已存在
        AdminUser existing = adminUserRepository.findByUsername(admin.getUsername());
        if (existing != null) {
            return Result.error("用户名已存在");
        }
        
        // 加密密码
        admin.setPassword(passwordEncoder.encode(admin.getPassword()));
        admin.setStatus(1);
        
        adminUserRepository.insert(admin);
        
        log.info("创建管理员: operatorId={}, newAdminId={}, username={}", 
                operatorId, admin.getId(), admin.getUsername());
        
        return Result.success(admin.getId());
    }
    
    /**
     * 更新管理员
     */
    @Transactional
    public Result<Void> updateAdmin(AdminUser admin, Long operatorId) {
        AdminUser operator = adminUserRepository.selectById(operatorId);
        if (operator == null) {
            return Result.error("无权操作");
        }
        
        // 只能修改自己或下级管理员
        if (!operatorId.equals(admin.getId()) && operator.getRole() != 1) {
            return Result.error("无权操作");
        }
        
        // 如果修改密码，需要加密
        if (admin.getPassword() != null && !admin.getPassword().isEmpty()) {
            admin.setPassword(passwordEncoder.encode(admin.getPassword()));
        } else {
            admin.setPassword(null); // 不修改密码
        }
        
        adminUserRepository.updateById(admin);
        
        log.info("更新管理员: operatorId={}, targetAdminId={}", operatorId, admin.getId());
        
        return Result.success();
    }
    
    /**
     * 删除管理员
     */
    @Transactional
    public Result<Void> deleteAdmin(Long adminId, Long operatorId) {
        AdminUser operator = adminUserRepository.selectById(operatorId);
        if (operator == null || operator.getRole() != 1) {
            return Result.error("无权操作");
        }
        
        // 不能删除自己
        if (adminId.equals(operatorId)) {
            return Result.error("不能删除自己");
        }
        
        adminUserRepository.deleteById(adminId);
        
        log.info("删除管理员: operatorId={}, targetAdminId={}", operatorId, adminId);
        
        return Result.success();
    }
    
    /**
     * 获取管理员列表
     */
    public Result<Page<AdminUser>> getAdminList(Integer page, Integer size, String keyword) {
        Page<AdminUser> pageParam = new Page<>(page, size);
        
        // 使用MyBatis Plus条件构造器
        com.baomidou.mybatisplus.core.conditions.query.QueryWrapper<AdminUser> wrapper = 
            new com.baomidou.mybatisplus.core.conditions.query.QueryWrapper<>();
        
        if (keyword != null && !keyword.isEmpty()) {
            wrapper.like("username", keyword)
                   .or()
                   .like("real_name", keyword);
        }
        
        wrapper.orderByDesc("create_time");
        
        Page<AdminUser> result = adminUserRepository.selectPage(pageParam, wrapper);
        
        // 不返回密码
        result.getRecords().forEach(admin -> admin.setPassword(null));
        
        return Result.success(result);
    }
    
    /**
     * 获取操作日志列表
     */
    public Result<Page<OperationLog>> getOperationLogs(Integer page, Integer size, 
                                                       Long adminId, String module) {
        Page<OperationLog> pageParam = new Page<>(page, size);
        
        com.baomidou.mybatisplus.core.conditions.query.QueryWrapper<OperationLog> wrapper = 
            new com.baomidou.mybatisplus.core.conditions.query.QueryWrapper<>();
        
        if (adminId != null) {
            wrapper.eq("admin_id", adminId);
        }
        if (module != null && !module.isEmpty()) {
            wrapper.eq("module", module);
        }
        
        wrapper.orderByDesc("create_time");
        
        Page<OperationLog> result = operationLogRepository.selectPage(pageParam, wrapper);
        
        return Result.success(result);
    }
    
    /**
     * 增加登录失败次数
     */
    private void incrementLoginAttempts(String key) {
        Long count = redisTemplate.opsForValue().increment(key);
        if (count != null && count == 1) {
            // 30分钟过期
            redisTemplate.expire(key, 30, TimeUnit.MINUTES);
        }
    }
    
    /**
     * 保存操作日志
     */
    private void saveOperationLog(Long adminId, String adminName, String operation, 
                                  String module, String description,
                                  String requestData, String responseData,
                                  String ip, Integer status, Long executionTime) {
        try {
            OperationLog log = new OperationLog();
            log.setAdminId(adminId);
            log.setAdminName(adminName);
            log.setOperation(operation);
            log.setModule(module);
            log.setDescription(description);
            log.setRequestData(requestData);
            log.setResponseData(responseData);
            log.setIp(ip);
            log.setStatus(status);
            log.setExecutionTime(executionTime);
            operationLogRepository.insert(log);
        } catch (Exception e) {
            // 日志保存失败不影响主流程
            log.error("保存操作日志失败", e);
        }
    }
}
