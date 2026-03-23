package com.ruoshan.service;

import com.ruoshan.dto.LoginRequest;
import com.ruoshan.dto.LoginResponse;
import com.ruoshan.entity.AdminUser;
import com.ruoshan.repository.AdminUserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.Optional;
import java.util.UUID;

@Service
public class AdminAuthService {
    
    @Autowired
    private AdminUserRepository adminUserRepository;
    
    private final BCryptPasswordEncoder passwordEncoder = new BCryptPasswordEncoder();
    
    public LoginResponse login(LoginRequest request) {
        Optional<AdminUser> userOpt = adminUserRepository.findByUsername(request.getUsername());
        
        if (userOpt.isEmpty()) {
            throw new RuntimeException("用户名或密码错误");
        }
        
        AdminUser user = userOpt.get();
        
        if (user.getStatus() != 1) {
            throw new RuntimeException("账户已被禁用");
        }
        
        if (!passwordEncoder.matches(request.getPassword(), user.getPassword())) {
            throw new RuntimeException("用户名或密码错误");
        }
        
        // 生成简单token（生产环境应使用JWT）
        String token = UUID.randomUUID().toString();
        
        LoginResponse response = new LoginResponse();
        response.setToken(token);
        response.setUsername(user.getUsername());
        response.setName(user.getName());
        response.setRole(user.getRole());
        
        return response;
    }
    
    public void changePassword(Long userId, String oldPassword, String newPassword) {
        AdminUser user = adminUserRepository.findById(userId)
                .orElseThrow(() -> new RuntimeException("用户不存在"));
        
        if (!passwordEncoder.matches(oldPassword, user.getPassword())) {
            throw new RuntimeException("原密码错误");
        }
        
        user.setPassword(passwordEncoder.encode(newPassword));
        adminUserRepository.save(user);
    }
}
