package com.dazi.vip.controller;

import com.dazi.common.result.Result;
import com.dazi.vip.dto.*;
import com.dazi.vip.entity.VipPrivilege;
import com.dazi.vip.service.VipService;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * VIP控制器
 */
@Slf4j
@RestController
@RequestMapping("/vip")
@RequiredArgsConstructor
public class VipController {
    
    private final VipService vipService;
    
    /**
     * 获取VIP套餐列表
     */
    @GetMapping("/packages")
    public Result<List<VipPackageDTO>> getPackages() {
        return Result.success(vipService.getPackages());
    }
    
    /**
     * 查询用户VIP状态
     */
    @GetMapping("/status")
    public Result<UserVipStatusDTO> getVipStatus(HttpServletRequest request) {
        Long userId = (Long) request.getAttribute("currentUserId");
        if (userId == null) {
            return Result.unauthorized();
        }
        return Result.success(vipService.getVipStatus(userId));
    }
    
    /**
     * 订阅VIP
     */
    @PostMapping("/subscribe")
    public Result<Void> subscribe(
            HttpServletRequest request,
            @Valid @RequestBody SubscribeVipRequest requestBody) {
        Long userId = (Long) request.getAttribute("currentUserId");
        if (userId == null) {
            return Result.unauthorized();
        }
        vipService.subscribe(userId, requestBody);
        return Result.success();
    }
    
    /**
     * 获取VIP特权列表
     */
    @GetMapping("/privileges")
    public Result<List<VipPrivilege>> getPrivileges() {
        return Result.success(vipService.getPrivileges());
    }
    
    /**
     * 检查用户是否是VIP（内部调用）
     */
    @GetMapping("/check/{userId}")
    public Result<Boolean> checkVip(@PathVariable Long userId) {
        return Result.success(vipService.isVip(userId));
    }
    
    /**
     * 检查用户是否有特定特权（内部调用）
     */
    @GetMapping("/check-privilege")
    public Result<Boolean> checkPrivilege(
            @RequestParam Long userId,
            @RequestParam String privilegeCode) {
        return Result.success(vipService.hasPrivilege(userId, privilegeCode));
    }
}
