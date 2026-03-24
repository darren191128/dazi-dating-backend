package com.dazi.common.util;

import com.auth0.jwt.JWT;
import com.auth0.jwt.JWTVerifier;
import com.auth0.jwt.algorithms.Algorithm;
import com.auth0.jwt.exceptions.JWTVerificationException;
import com.auth0.jwt.interfaces.DecodedJWT;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Component;

import java.util.Date;
import java.util.concurrent.TimeUnit;

/**
 * JWT工具类 - 增强版
 * 支持Token黑名单机制
 */
@Slf4j
@Component
public class JwtUtil {
    
    @Value("${jwt.secret}")
    private String secret;
    
    @Value("${jwt.expiration:86400000}")
    private Long expiration;
    
    @Autowired
    private RedisTemplate<String, Object> redisTemplate;
    
    private static final String TOKEN_BLACKLIST_PREFIX = "token:blacklist:";
    
    /**
     * 生成token
     */
    public String generateToken(Long userId, String openId) {
        Date now = new Date();
        Date expiryDate = new Date(now.getTime() + expiration);
        
        String token = JWT.create()
                .withSubject(String.valueOf(userId))
                .withClaim("userId", userId)
                .withClaim("openId", openId)
                .withIssuedAt(now)
                .withExpiresAt(expiryDate)
                .withJWTId(generateJti())
                .sign(Algorithm.HMAC256(secret));
        
        // 将token存入Redis，用于主动失效
        String tokenKey = TOKEN_BLACKLIST_PREFIX + "valid:" + userId;
        redisTemplate.opsForValue().set(tokenKey, token, expiration, TimeUnit.MILLISECONDS);
        
        return token;
    }
    
    /**
     * 从token中获取用户ID
     */
    public Long getUserIdFromToken(String token) {
        try {
            DecodedJWT jwt = JWT.decode(token);
            return jwt.getClaim("userId").asLong();
        } catch (Exception e) {
            log.error("解析token失败: {}", e.getMessage());
            return null;
        }
    }
    
    /**
     * 验证token（包括黑名单检查）
     */
    public boolean validateToken(String token) {
        try {
            // 1. 检查是否在黑名单
            if (isTokenBlacklisted(token)) {
                log.warn("Token已在黑名单: {}", maskToken(token));
                return false;
            }
            
            // 2. 验证签名和过期时间
            Algorithm algorithm = Algorithm.HMAC256(secret);
            JWTVerifier verifier = JWT.require(algorithm).build();
            verifier.verify(token);
            
            return true;
        } catch (JWTVerificationException e) {
            log.error("Token验证失败: {}", e.getMessage());
            return false;
        }
    }
    
    /**
     * 将token加入黑名单（退出登录时使用）
     */
    public void blacklistToken(String token) {
        try {
            DecodedJWT jwt = JWT.decode(token);
            Date expiresAt = jwt.getExpiresAt();
            String jti = jwt.getId();
            
            if (expiresAt != null && jti != null) {
                long ttl = expiresAt.getTime() - System.currentTimeMillis();
                if (ttl > 0) {
                    redisTemplate.opsForValue().set(
                        TOKEN_BLACKLIST_PREFIX + jti, 
                        "blacklisted", 
                        ttl, 
                        TimeUnit.MILLISECONDS
                    );
                    log.info("Token已加入黑名单: {}", maskToken(token));
                }
            }
        } catch (Exception e) {
            log.error("Token加入黑名单失败: {}", e.getMessage());
        }
    }
    
    /**
     * 检查token是否在黑名单
     */
    private boolean isTokenBlacklisted(String token) {
        try {
            DecodedJWT jwt = JWT.decode(token);
            String jti = jwt.getId();
            if (jti != null) {
                return Boolean.TRUE.equals(redisTemplate.hasKey(TOKEN_BLACKLIST_PREFIX + jti));
            }
        } catch (Exception e) {
            log.error("检查Token黑名单失败: {}", e.getMessage());
        }
        return false;
    }
    
    /**
     * 判断token是否过期
     */
    public boolean isTokenExpired(String token) {
        try {
            DecodedJWT jwt = JWT.decode(token);
            return jwt.getExpiresAt().before(new Date());
        } catch (Exception e) {
            return true;
        }
    }
    
    /**
     * 刷新token
     */
    public String refreshToken(String token) {
        Long userId = getUserIdFromToken(token);
        if (userId == null) {
            return null;
        }
        
        DecodedJWT jwt = JWT.decode(token);
        String openId = jwt.getClaim("openId").asString();
        
        // 将旧token加入黑名单
        blacklistToken(token);
        
        return generateToken(userId, openId);
    }
    
    /**
     * 生成JWT ID
     */
    private String generateJti() {
        return java.util.UUID.randomUUID().toString().replace("-", "");
    }
    
    /**
     * 脱敏显示token
     */
    private String maskToken(String token) {
        if (token == null || token.length() < 10) {
            return "***";
        }
        return token.substring(0, 6) + "..." + token.substring(token.length() - 4);
    }
}
