package com.ruoshan.controller;

import com.ruoshan.dto.LoginRequest;
import com.ruoshan.dto.LoginResponse;
import com.ruoshan.service.AdminAuthService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.Map;

@RestController
@RequestMapping("/api/admin")
@CrossOrigin(origins = "*")
public class AdminAuthController {
    
    @Autowired
    private AdminAuthService adminAuthService;
    
    @PostMapping("/login")
    public ResponseEntity<LoginResponse> login(@RequestBody LoginRequest request) {
        LoginResponse response = adminAuthService.login(request);
        return ResponseEntity.ok(response);
    }
    
    @PostMapping("/logout")
    public ResponseEntity<Map<String, String>> logout() {
        Map<String, String> result = new HashMap<>();
        result.put("message", "退出成功");
        return ResponseEntity.ok(result);
    }
    
    @GetMapping("/profile")
    public ResponseEntity<Map<String, Object>> getProfile() {
        // 这里应该从token中获取用户信息
        Map<String, Object> profile = new HashMap<>();
        profile.put("username", "admin");
        profile.put("name", "系统管理员");
        profile.put("role", "SUPER_ADMIN");
        return ResponseEntity.ok(profile);
    }
}
