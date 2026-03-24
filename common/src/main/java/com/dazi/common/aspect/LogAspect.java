package com.dazi.common.aspect;

import com.dazi.common.annotation.Log;
import com.dazi.common.util.JwtUtil;
import jakarta.servlet.http.HttpServletRequest;
import lombok.extern.slf4j.Slf4j;
import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.annotation.AfterReturning;
import org.aspectj.lang.annotation.AfterThrowing;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Before;
import org.aspectj.lang.reflect.MethodSignature;
import org.springframework.stereotype.Component;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import java.lang.reflect.Method;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;
import java.util.stream.Collectors;

/**
 * 操作日志切面
 * 拦截带有@Log注解的方法，记录操作日志
 */
@Slf4j
@Aspect
@Component
public class LogAspect {

    private static final ThreadLocal<Long> START_TIME = new ThreadLocal<>();
    
    /**
     * 敏感字段列表，需要脱敏处理
     */
    private static final String[] SENSITIVE_FIELDS = {
        "password", "pwd", "passwd", "token", "secret", "key", 
        "creditCard", "idCard", "phone", "mobile", "email"
    };

    /**
     * 方法执行前记录
     */
    @Before("@annotation(com.dazi.common.annotation.Log)")
    public void doBefore(JoinPoint joinPoint) {
        START_TIME.set(System.currentTimeMillis());
        
        try {
            MethodSignature signature = (MethodSignature) joinPoint.getSignature();
            Method method = signature.getMethod();
            Log logAnnotation = method.getAnnotation(Log.class);
            
            HttpServletRequest request = getRequest();
            if (request == null) return;
            
            Long userId = getCurrentUserId(request);
            String operation = logAnnotation.operation();
            String type = logAnnotation.type();
            
            StringBuilder logMsg = new StringBuilder();
            logMsg.append("[操作日志] ")
                  .append("类型=").append(type)
                  .append(", 操作=").append(operation)
                  .append(", 用户ID=").append(userId)
                  .append(", IP=").append(getClientIp(request));
            
            // 记录请求参数
            if (logAnnotation.logParams()) {
                Object[] args = joinPoint.getArgs();
                if (args != null && args.length > 0) {
                    String params = Arrays.stream(args)
                            .filter(arg -> !(arg instanceof HttpServletRequest))
                            .map(this::maskSensitiveData)
                            .collect(Collectors.joining(", "));
                    if (!params.isEmpty()) {
                        logMsg.append(", 参数=").append(params);
                    }
                }
            }
            
            log.info(logMsg.toString());
            
        } catch (Exception e) {
            log.error("记录操作日志失败", e);
        }
    }

    /**
     * 方法正常返回后记录
     */
    @AfterReturning(pointcut = "@annotation(com.dazi.common.annotation.Log)", returning = "result")
    public void doAfterReturning(JoinPoint joinPoint, Object result) {
        try {
            long costTime = System.currentTimeMillis() - START_TIME.get();
            START_TIME.remove();
            
            MethodSignature signature = (MethodSignature) joinPoint.getSignature();
            Method method = signature.getMethod();
            Log logAnnotation = method.getAnnotation(Log.class);
            
            StringBuilder logMsg = new StringBuilder();
            logMsg.append("[操作完成] ")
                  .append("操作=").append(logAnnotation.operation())
                  .append(", 耗时=").append(costTime).append("ms");
            
            if (logAnnotation.logResult() && result != null) {
                logMsg.append(", 结果=").append(maskSensitiveData(result));
            }
            
            log.info(logMsg.toString());
            
        } catch (Exception e) {
            log.error("记录操作完成日志失败", e);
        }
    }

    /**
     * 方法抛出异常后记录
     */
    @AfterThrowing(pointcut = "@annotation(com.dazi.common.annotation.Log)", throwing = "e")
    public void doAfterThrowing(JoinPoint joinPoint, Throwable e) {
        try {
            long costTime = System.currentTimeMillis() - START_TIME.get();
            START_TIME.remove();
            
            MethodSignature signature = (MethodSignature) joinPoint.getSignature();
            Method method = signature.getMethod();
            Log logAnnotation = method.getAnnotation(Log.class);
            
            log.error("[操作异常] 操作={}, 耗时={}ms, 异常={}", 
                    logAnnotation.operation(), costTime, e.getMessage(), e);
            
        } catch (Exception ex) {
            log.error("记录操作异常日志失败", ex);
        }
    }

    /**
     * 获取当前请求
     */
    private HttpServletRequest getRequest() {
        ServletRequestAttributes attributes = (ServletRequestAttributes) RequestContextHolder.getRequestAttributes();
        return attributes != null ? attributes.getRequest() : null;
    }

    /**
     * 获取当前用户ID
     */
    private Long getCurrentUserId(HttpServletRequest request) {
        Object userId = request.getAttribute("currentUserId");
        return userId != null ? (Long) userId : null;
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
     * 敏感数据脱敏
     */
    private String maskSensitiveData(Object obj) {
        if (obj == null) return "null";
        
        String str = obj.toString();
        
        // 对敏感字段进行脱敏
        for (String field : SENSITIVE_FIELDS) {
            if (str.contains(field)) {
                // 简单脱敏：将敏感值替换为***
                str = str.replaceAll("(" + field + "[=:])([^,}]+)", "$1***");
            }
        }
        
        // 如果字符串过长，截断显示
        if (str.length() > 500) {
            str = str.substring(0, 500) + "...";
        }
        
        return str;
    }
}
