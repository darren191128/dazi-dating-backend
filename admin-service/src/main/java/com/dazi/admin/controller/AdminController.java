package com.dazi.admin.controller;

import com.dazi.admin.dto.HandleReportRequest;
import com.dazi.admin.dto.PageResult;
import com.dazi.admin.dto.ReportDTO;
import com.dazi.admin.dto.ReportUserRequest;
import com.dazi.admin.entity.AdminUser;
import com.dazi.admin.service.AdminService;
import com.dazi.admin.service.ReportService;
import com.dazi.common.result.Result;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

/**
 * 管理员控制器
 */
@RestController
@RequestMapping("/api/admin")
@RequiredArgsConstructor
public class AdminController {
    
    private final AdminService adminService;
    private final ReportService reportService;
    
    /**
     * 管理员登录
     */
    @PostMapping("/login")
    public Result<Map<String, Object>> login(@RequestBody Map<String, String> params, 
                                             HttpServletRequest request) {
        String username = params.get("username");
        String password = params.get("password");
        String ip = getClientIp(request);
        
        return adminService.login(username, password, ip);
    }
    
    /**
     * 退出登录
     */
    @PostMapping("/logout")
    public Result<Void> logout(HttpServletRequest request) {
        Long adminId = (Long) request.getAttribute("currentUserId");
        String token = (String) request.getAttribute("currentToken");
        return adminService.logout(adminId, token);
    }
    
    /**
     * 获取当前管理员信息
     */
    @GetMapping("/info")
    public Result<AdminUser> getAdminInfo(HttpServletRequest request) {
        Long adminId = (Long) request.getAttribute("currentUserId");
        return adminService.getAdminInfo(adminId);
    }
    
    /**
     * 获取管理员列表
     */
    @GetMapping("/list")
    public Result<?> getAdminList(
            @RequestParam(defaultValue = "1") Integer page,
            @RequestParam(defaultValue = "10") Integer size,
            @RequestParam(required = false) String keyword) {
        return adminService.getAdminList(page, size, keyword);
    }
    
    /**
     * 创建管理员
     */
    @PostMapping
    public Result<Long> createAdmin(@RequestBody AdminUser admin, HttpServletRequest request) {
        Long operatorId = (Long) request.getAttribute("currentUserId");
        return adminService.createAdmin(admin, operatorId);
    }
    
    /**
     * 更新管理员
     */
    @PutMapping("/{adminId}")
    public Result<Void> updateAdmin(@PathVariable Long adminId, 
                                    @RequestBody AdminUser admin,
                                    HttpServletRequest request) {
        Long operatorId = (Long) request.getAttribute("currentUserId");
        admin.setId(adminId);
        return adminService.updateAdmin(admin, operatorId);
    }
    
    /**
     * 删除管理员
     */
    @DeleteMapping("/{adminId}")
    public Result<Void> deleteAdmin(@PathVariable Long adminId, HttpServletRequest request) {
        Long operatorId = (Long) request.getAttribute("currentUserId");
        return adminService.deleteAdmin(adminId, operatorId);
    }
    
    /**
     * 获取操作日志
     */
    @GetMapping("/logs")
    public Result<?> getOperationLogs(
            @RequestParam(defaultValue = "1") Integer page,
            @RequestParam(defaultValue = "20") Integer size,
            @RequestParam(required = false) Long adminId,
            @RequestParam(required = false) String module) {
        return adminService.getOperationLogs(page, size, adminId, module);
    }
    
    // ==================== 举报相关接口 ====================

    /**
     * 举报用户
     */
    @PostMapping("/report")
    public Result<Void> reportUser(
            HttpServletRequest request,
            @Valid @RequestBody ReportUserRequest requestBody) {
        Long userId = (Long) request.getAttribute("currentUserId");
        return reportService.reportUser(userId, requestBody);
    }

    /**
     * 获取举报列表（后台）
     */
    @GetMapping("/reports")
    public Result<PageResult<ReportDTO>> getReports(
            HttpServletRequest request,
            @RequestParam(required = false) Integer status,
            @RequestParam(defaultValue = "1") Integer page,
            @RequestParam(defaultValue = "20") Integer pageSize) {
        return reportService.getReports(status, page, pageSize);
    }

    /**
     * 处理举报（后台）
     */
    @PutMapping("/reports/{id}/handle")
    public Result<Void> handleReport(
            HttpServletRequest request,
            @PathVariable Long id,
            @Valid @RequestBody HandleReportRequest requestBody) {
        Long handlerId = (Long) request.getAttribute("currentUserId");
        return reportService.handleReport(id, handlerId, requestBody);
    }

    /**
     * 获取客户端IP
     */
    private String getClientIp(HttpServletRequest request) {
        String ip = request.getHeader("X-Forwarded-For");
        if (ip == null || ip.isEmpty() || "unknown".equalsIgnoreCase(ip)) {
            ip = request.getHeader("Proxy-Client-IP");
        }
        if (ip == null || ip.isEmpty() || "unknown".equalsIgnoreCase(ip)) {
            ip = request.getHeader("WL-Proxy-Client-IP");
        }
        if (ip == null || ip.isEmpty() || "unknown".equalsIgnoreCase(ip)) {
            ip = request.getRemoteAddr();
        }
        if (ip != null && ip.contains(",")) {
            ip = ip.split(",")[0].trim();
        }
        return ip;
    }
}
