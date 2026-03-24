package com.dazi.admin.controller;

import com.dazi.activity.entity.Activity;
import com.dazi.admin.service.OperationService;
import com.dazi.common.result.Result;
import com.dazi.user.entity.User;
import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

/**
 * 运营管理控制器
 */
@RestController
@RequestMapping("/api/admin/operation")
@RequiredArgsConstructor
public class OperationController {
    
    private final OperationService operationService;
    
    // ==================== 会员管理 ====================
    
    /**
     * 获取会员列表
     */
    @GetMapping("/users")
    public Result<?> getUserList(
            @RequestParam(defaultValue = "1") Integer page,
            @RequestParam(defaultValue = "20") Integer size,
            @RequestParam(required = false) String keyword,
            @RequestParam(required = false) Integer status,
            @RequestParam(required = false) Integer gender) {
        return operationService.getUserList(page, size, keyword, status, gender);
    }
    
    /**
     * 获取会员详情
     */
    @GetMapping("/users/{userId}")
    public Result<User> getUserDetail(@PathVariable Long userId) {
        return operationService.getUserDetail(userId);
    }
    
    /**
     * 编辑会员
     */
    @PutMapping("/users/{userId}")
    public Result<Void> updateUser(@PathVariable Long userId, 
                                   @RequestBody User user,
                                   HttpServletRequest request) {
        Long operatorId = (Long) request.getAttribute("currentUserId");
        return operationService.updateUser(userId, user, operatorId);
    }
    
    /**
     * 删除会员
     */
    @DeleteMapping("/users/{userId}")
    public Result<Void> deleteUser(@PathVariable Long userId, HttpServletRequest request) {
        Long operatorId = (Long) request.getAttribute("currentUserId");
        return operationService.deleteUser(userId, operatorId);
    }
    
    /**
     * 禁用/启用会员
     */
    @PostMapping("/users/{userId}/toggle-status")
    public Result<Void> toggleUserStatus(@PathVariable Long userId,
                                         @RequestParam Integer status,
                                         HttpServletRequest request) {
        Long operatorId = (Long) request.getAttribute("currentUserId");
        return operationService.toggleUserStatus(userId, status, operatorId);
    }
    
    // ==================== 活动管理 ====================
    
    /**
     * 获取活动列表
     */
    @GetMapping("/activities")
    public Result<?> getActivityList(
            @RequestParam(defaultValue = "1") Integer page,
            @RequestParam(defaultValue = "20") Integer size,
            @RequestParam(required = false) String keyword,
            @RequestParam(required = false) Integer status,
            @RequestParam(required = false) Integer type) {
        return operationService.getActivityList(page, size, keyword, status, type);
    }
    
    /**
     * 获取活动详情
     */
    @GetMapping("/activities/{activityId}")
    public Result<Activity> getActivityDetail(@PathVariable Long activityId) {
        return operationService.getActivityDetail(activityId);
    }
    
    /**
     * 编辑活动
     */
    @PutMapping("/activities/{activityId}")
    public Result<Void> updateActivity(@PathVariable Long activityId,
                                       @RequestBody Activity activity,
                                       HttpServletRequest request) {
        Long operatorId = (Long) request.getAttribute("currentUserId");
        return operationService.updateActivity(activityId, activity, operatorId);
    }
    
    /**
     * 删除活动
     */
    @DeleteMapping("/activities/{activityId}")
    public Result<Void> deleteActivity(@PathVariable Long activityId, HttpServletRequest request) {
        Long operatorId = (Long) request.getAttribute("currentUserId");
        return operationService.deleteActivity(activityId, operatorId);
    }
    
    /**
     * 审核活动
     */
    @PostMapping("/activities/{activityId}/audit")
    public Result<Void> auditActivity(@PathVariable Long activityId,
                                      @RequestParam Integer status,
                                      @RequestParam(required = false) String reason,
                                      HttpServletRequest request) {
        Long operatorId = (Long) request.getAttribute("currentUserId");
        return operationService.auditActivity(activityId, status, reason, operatorId);
    }
    
    // ==================== 数据统计 ====================
    
    /**
     * 获取运营数据概览
     */
    @GetMapping("/dashboard")
    public Result<Map<String, Object>> getDashboardData() {
        return operationService.getDashboardData();
    }
}
