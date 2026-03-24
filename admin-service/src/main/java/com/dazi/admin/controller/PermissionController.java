package com.dazi.admin.controller;

import com.dazi.admin.entity.AdminPermission;
import com.dazi.admin.service.PermissionService;
import com.dazi.common.result.Result;
import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;
import java.util.Set;

/**
 * 权限管理控制器
 */
@RestController
@RequestMapping("/api/admin/permission")
@RequiredArgsConstructor
public class PermissionController {
    
    private final PermissionService permissionService;
    
    /**
     * 获取所有权限列表
     */
    @GetMapping("/list")
    public Result<List<AdminPermission>> getAllPermissions() {
        return permissionService.getAllPermissions();
    }
    
    /**
     * 获取角色的权限列表
     */
    @GetMapping("/role/{role}")
    public Result<List<AdminPermission>> getRolePermissions(@PathVariable Integer role) {
        return permissionService.getRolePermissions(role);
    }
    
    /**
     * 获取当前管理员的权限列表
     */
    @GetMapping("/my")
    public Result<Set<String>> getMyPermissions(HttpServletRequest request) {
        Long adminId = (Long) request.getAttribute("currentUserId");
        return permissionService.getAdminPermissions(adminId);
    }
    
    /**
     * 检查是否有权限
     */
    @GetMapping("/check")
    public Result<Boolean> hasPermission(HttpServletRequest request,
                                         @RequestParam String permissionCode) {
        Long adminId = (Long) request.getAttribute("currentUserId");
        return permissionService.hasPermission(adminId, permissionCode);
    }
    
    /**
     * 为角色分配权限
     */
    @PostMapping("/assign/{role}")
    public Result<Void> assignPermissions(@PathVariable Integer role,
                                          @RequestBody List<Long> permissionIds,
                                          HttpServletRequest request) {
        Long operatorId = (Long) request.getAttribute("currentUserId");
        return permissionService.assignPermissionsToRole(role, permissionIds, operatorId);
    }
    
    /**
     * 创建权限
     */
    @PostMapping
    public Result<Long> createPermission(@RequestBody AdminPermission permission,
                                         HttpServletRequest request) {
        Long operatorId = (Long) request.getAttribute("currentUserId");
        return permissionService.createPermission(permission, operatorId);
    }
    
    /**
     * 删除权限
     */
    @DeleteMapping("/{permissionId}")
    public Result<Void> deletePermission(@PathVariable Long permissionId,
                                         HttpServletRequest request) {
        Long operatorId = (Long) request.getAttribute("currentUserId");
        return permissionService.deletePermission(permissionId, operatorId);
    }
    
    /**
     * 获取权限矩阵
     */
    @GetMapping("/matrix")
    public Result<Map<String, Object>> getPermissionMatrix() {
        return permissionService.getPermissionMatrix();
    }
}
