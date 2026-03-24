package com.dazi.admin.service;

import com.dazi.admin.entity.AdminPermission;
import com.dazi.admin.entity.AdminRolePermission;
import com.dazi.admin.entity.AdminUser;
import com.dazi.admin.repository.AdminPermissionRepository;
import com.dazi.admin.repository.AdminRolePermissionRepository;
import com.dazi.admin.repository.AdminUserRepository;
import com.dazi.common.result.Result;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;

/**
 * 权限管理服务
 */
@Slf4j
@Service
@RequiredArgsConstructor
public class PermissionService {
    
    private final AdminPermissionRepository permissionRepository;
    private final AdminRolePermissionRepository rolePermissionRepository;
    private final AdminUserRepository adminUserRepository;
    private final RedisTemplate<String, Object> redisTemplate;
    
    private static final String PERMISSION_CACHE_PREFIX = "permission:cache:";
    
    /**
     * 获取所有权限列表
     */
    public Result<List<AdminPermission>> getAllPermissions() {
        List<AdminPermission> permissions = permissionRepository.selectList(null);
        return Result.success(permissions);
    }
    
    /**
     * 获取角色的权限列表
     */
    public Result<List<AdminPermission>> getRolePermissions(Integer role) {
        String cacheKey = PERMISSION_CACHE_PREFIX + "role:" + role;
        
        @SuppressWarnings("unchecked")
        List<AdminPermission> cached = (List<AdminPermission>) redisTemplate.opsForValue().get(cacheKey);
        if (cached != null) {
            return Result.success(cached);
        }
        
        List<AdminPermission> permissions = permissionRepository.findByRole(role);
        
        // 缓存1小时
        redisTemplate.opsForValue().set(cacheKey, permissions, 1, java.util.concurrent.TimeUnit.HOURS);
        
        return Result.success(permissions);
    }
    
    /**
     * 获取管理员的权限列表
     */
    public Result<Set<String>> getAdminPermissions(Long adminId) {
        String cacheKey = PERMISSION_CACHE_PREFIX + "admin:" + adminId;
        
        @SuppressWarnings("unchecked")
        Set<String> cached = (Set<String>) redisTemplate.opsForValue().get(cacheKey);
        if (cached != null) {
            return Result.success(cached);
        }
        
        AdminUser admin = adminUserRepository.selectById(adminId);
        if (admin == null) {
            return Result.error("管理员不存在");
        }
        
        List<AdminPermission> permissions = permissionRepository.findByRole(admin.getRole());
        Set<String> permissionCodes = permissions.stream()
                .map(AdminPermission::getPermissionCode)
                .collect(Collectors.toSet());
        
        // 缓存1小时
        redisTemplate.opsForValue().set(cacheKey, permissionCodes, 1, java.util.concurrent.TimeUnit.HOURS);
        
        return Result.success(permissionCodes);
    }
    
    /**
     * 检查管理员是否有权限
     */
    public Result<Boolean> hasPermission(Long adminId, String permissionCode) {
        Result<Set<String>> permissionsResult = getAdminPermissions(adminId);
        if (!permissionsResult.isSuccess()) {
            return Result.success(false);
        }
        
        Set<String> permissions = permissionsResult.getData();
        return Result.success(permissions.contains(permissionCode));
    }
    
    /**
     * 为角色分配权限
     */
    @Transactional
    public Result<Void> assignPermissionsToRole(Integer role, List<Long> permissionIds, Long operatorId) {
        // 检查操作权限
        AdminUser operator = adminUserRepository.selectById(operatorId);
        if (operator == null || operator.getRole() != 1) {
            return Result.error("无权操作");
        }
        
        // 删除原有权限
        com.baomidou.mybatisplus.core.conditions.query.QueryWrapper<AdminRolePermission> wrapper = 
            new com.baomidou.mybatisplus.core.conditions.query.QueryWrapper<>();
        wrapper.eq("role", role);
        rolePermissionRepository.delete(wrapper);
        
        // 添加新权限
        for (Long permissionId : permissionIds) {
            AdminRolePermission rp = new AdminRolePermission();
            rp.setRole(role);
            rp.setPermissionId(permissionId);
            rolePermissionRepository.insert(rp);
        }
        
        // 清除缓存
        redisTemplate.delete(PERMISSION_CACHE_PREFIX + "role:" + role);
        
        log.info("分配角色权限: operatorId={}, role={}, permissionCount={}", 
                operatorId, role, permissionIds.size());
        
        return Result.success();
    }
    
    /**
     * 创建权限
     */
    @Transactional
    public Result<Long> createPermission(AdminPermission permission, Long operatorId) {
        AdminUser operator = adminUserRepository.selectById(operatorId);
        if (operator == null || operator.getRole() != 1) {
            return Result.error("无权操作");
        }
        
        permissionRepository.insert(permission);
        
        log.info("创建权限: operatorId={}, permissionCode={}", 
                operatorId, permission.getPermissionCode());
        
        return Result.success(permission.getId());
    }
    
    /**
     * 删除权限
     */
    @Transactional
    public Result<Void> deletePermission(Long permissionId, Long operatorId) {
        AdminUser operator = adminUserRepository.selectById(operatorId);
        if (operator == null || operator.getRole() != 1) {
            return Result.error("无权操作");
        }
        
        permissionRepository.deleteById(permissionId);
        
        // 删除角色权限关联
        com.baomidou.mybatisplus.core.conditions.query.QueryWrapper<AdminRolePermission> wrapper = 
            new com.baomidou.mybatisplus.core.conditions.query.QueryWrapper<>();
        wrapper.eq("permission_id", permissionId);
        rolePermissionRepository.delete(wrapper);
        
        // 清除所有角色缓存
        redisTemplate.delete(PERMISSION_CACHE_PREFIX + "role:1");
        redisTemplate.delete(PERMISSION_CACHE_PREFIX + "role:2");
        redisTemplate.delete(PERMISSION_CACHE_PREFIX + "role:3");
        
        log.info("删除权限: operatorId={}, permissionId={}", operatorId, permissionId);
        
        return Result.success();
    }
    
    /**
     * 清除管理员权限缓存
     */
    public void clearAdminPermissionCache(Long adminId) {
        redisTemplate.delete(PERMISSION_CACHE_PREFIX + "admin:" + adminId);
    }
    
    /**
     * 获取权限矩阵（用于前端展示）
     */
    public Result<Map<String, Object>> getPermissionMatrix() {
        List<AdminPermission> allPermissions = permissionRepository.selectList(null);
        
        Map<String, Object> matrix = new HashMap<>();
        matrix.put("permissions", allPermissions);
        
        // 角色权限映射
        Map<Integer, List<Long>> rolePermissions = new HashMap<>();
        for (int role = 1; role <= 3; role++) {
            List<AdminPermission> permissions = permissionRepository.findByRole(role);
            rolePermissions.put(role, permissions.stream()
                    .map(AdminPermission::getId)
                    .collect(Collectors.toList()));
        }
        matrix.put("rolePermissions", rolePermissions);
        
        return Result.success(matrix);
    }
}
