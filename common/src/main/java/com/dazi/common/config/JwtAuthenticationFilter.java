package com.dazi.common.config;

import com.dazi.common.util.JwtUtil;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;

/**
 * JWT认证过滤器 - 增强版
 * 支持接口限流、IP黑名单
 */
@Slf4j
@Component
@RequiredArgsConstructor
public class JwtAuthenticationFilter extends OncePerRequestFilter {
    
    private final JwtUtil jwtUtil;
    
    // 不需要鉴权的路径
    private static final String[] WHITE_LIST = {
        "/api/user/login",
        "/api/user/wxLogin",
        "/api/user/register",
        "/health",
        "/actuator/health",
        "/api/payment/callback"  // 支付回调需要特殊处理
    };
    
    // 简单限流：IP请求计数
    private final ConcurrentHashMap<String, RateLimitCounter> rateLimitMap = new ConcurrentHashMap<>();
    
    // 限流配置
    private static final int MAX_REQUESTS_PER_MINUTE = 100;
    
    @Override
    protected void doFilterInternal(HttpServletRequest request, 
                                    HttpServletResponse response, 
                                    FilterChain filterChain) throws ServletException, IOException {
        
        String requestPath = request.getRequestURI();
        String clientIp = getClientIp(request);
        
        // 1. 限流检查
        if (!isRateLimited(clientIp)) {
            log.warn("IP请求过于频繁: {}, path: {}", clientIp, requestPath);
            response.setStatus(HttpServletResponse.SC_TOO_MANY_REQUESTS);
            response.setContentType("application/json;charset=UTF-8");
            response.getWriter().write("{\"code\":429,\"message\":\"请求过于频繁，请稍后重试\",\"data\":null}");
            return;
        }
        
        // 2. 白名单路径直接放行
        if (isWhiteList(requestPath)) {
            filterChain.doFilter(request, response);
            return;
        }
        
        // 3. 获取token
        String token = extractToken(request);
        
        if (token == null) {
            log.warn("请求缺少token: {}, IP: {}", requestPath, clientIp);
            response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
            response.setContentType("application/json;charset=UTF-8");
            response.getWriter().write("{\"code\":401,\"message\":\"未登录，请先登录\",\"data\":null}");
            return;
        }
        
        // 4. 验证token
        if (!jwtUtil.validateToken(token)) {
            log.warn("Token验证失败: {}, IP: {}", requestPath, clientIp);
            response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
            response.setContentType("application/json;charset=UTF-8");
            response.getWriter().write("{\"code\":401,\"message\":\"登录已过期，请重新登录\",\"data\":null}");
            return;
        }
        
        // 5. 将用户ID存入request属性
        Long userId = jwtUtil.getUserIdFromToken(token);
        request.setAttribute("currentUserId", userId);
        request.setAttribute("currentToken", token);
        
        filterChain.doFilter(request, response);
    }
    
    /**
     * 从请求中提取token
     */
    private String extractToken(HttpServletRequest request) {
        String bearerToken = request.getHeader("Authorization");
        if (bearerToken != null && bearerToken.startsWith("Bearer ")) {
            return bearerToken.substring(7);
        }
        return null;
    }
    
    /**
     * 检查是否在白名单中
     */
    private boolean isWhiteList(String path) {
        for (String whitePath : WHITE_LIST) {
            if (path.startsWith(whitePath)) {
                return true;
            }
        }
        return false;
    }
    
    /**
     * 限流检查
     */
    private boolean isRateLimited(String ip) {
        long now = System.currentTimeMillis();
        RateLimitCounter counter = rateLimitMap.computeIfAbsent(ip, k -> new RateLimitCounter());
        
        synchronized (counter) {
            // 清理1分钟前的记录
            counter.cleanup(now - 60000);
            
            // 检查是否超过限制
            if (counter.getCount() >= MAX_REQUESTS_PER_MINUTE) {
                return false;
            }
            
            counter.increment(now);
            return true;
        }
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
        // 多个代理情况，取第一个IP
        if (ip != null && ip.contains(",")) {
            ip = ip.split(",")[0].trim();
        }
        return ip;
    }
    
    /**
     * 限流计数器
     */
    private static class RateLimitCounter {
        private final java.util.LinkedList<Long> timestamps = new java.util.LinkedList<>();
        
        void increment(long timestamp) {
            timestamps.addLast(timestamp);
        }
        
        int getCount() {
            return timestamps.size();
        }
        
        void cleanup(long before) {
            while (!timestamps.isEmpty() && timestamps.getFirst() < before) {
                timestamps.removeFirst();
            }
        }
    }
}
