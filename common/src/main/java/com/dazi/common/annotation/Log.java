package com.dazi.common.annotation;

import java.lang.annotation.*;

/**
 * 操作日志注解
 * 用于标记需要记录操作日志的方法
 * 
 * 使用示例：
 * @Log(operation = "用户登录", type = "LOGIN")
 * public Result<?> login(LoginDTO dto) { ... }
 */
@Target(ElementType.METHOD)
@Retention(RetentionPolicy.RUNTIME)
@Documented
public @interface Log {
    
    /**
     * 操作描述
     */
    String operation() default "";
    
    /**
     * 操作类型
     * LOGIN - 登录
     * LOGOUT - 登出
     * CREATE - 创建
     * UPDATE - 更新
     * DELETE - 删除
     * QUERY - 查询
     * PAYMENT - 支付
     * OTHER - 其他
     */
    String type() default "OTHER";
    
    /**
     * 是否记录请求参数
     */
    boolean logParams() default true;
    
    /**
     * 是否记录响应结果
     */
    boolean logResult() default false;
    
    /**
     * 敏感参数，需要脱敏处理
     */
    String[] sensitiveParams() default {};
}
