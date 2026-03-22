package com.dazi.user.controller;

import com.dazi.common.result.Result;
import com.dazi.user.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

@RestController
@RequestMapping("/api/user")
@RequiredArgsConstructor
public class UserController {
    
    private final UserService userService;
    
    /**
     * 微信登录
     */
    @PostMapping("/login/wx")
    public Result<Map<String, Object>> wxLogin(
            @RequestParam String openId,
            @RequestParam String nickname,
            @RequestParam String avatar,
            @RequestParam Integer gender) {
        return userService.wxLogin(openId, nickname, avatar, gender);
    }
    
    /**
     * 获取用户信息
     */
    @GetMapping("/info/{userId}")
    public Result<Map<String, Object>> getUserInfo(@PathVariable Long userId) {
        return userService.getUserInfo(userId);
    }
    
    /**
     * 更新用户资料
     */
    @PutMapping("/profile/{userId}")
    public Result<Void> updateProfile(
            @PathVariable Long userId,
            @RequestParam(required = false) String nickname,
            @RequestParam(required = false) String bio,
            @RequestParam(required = false) Integer age) {
        return userService.updateProfile(userId, nickname, bio, age);
    }
}
