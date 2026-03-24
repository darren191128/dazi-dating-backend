package com.dazi.common.result;

import lombok.Data;

import java.io.Serializable;

/**
 * 统一返回结果
 */
@Data
public class Result<T> implements Serializable {
    
    private static final long serialVersionUID = 1L;
    
    /**
     * 状态码：200成功，其他失败
     */
    private Integer code;
    
    /**
     * 返回消息
     */
    private String message;
    
    /**
     * 返回数据
     */
    private T data;
    
    /**
     * 时间戳
     */
    private Long timestamp;
    
    public Result() {
        this.timestamp = System.currentTimeMillis();
    }
    
    public Result(Integer code, String message, T data) {
        this.code = code;
        this.message = message;
        this.data = data;
        this.timestamp = System.currentTimeMillis();
    }
    
    /**
     * 成功返回结果
     */
    public static <T> Result<T> success() {
        return new Result<>(200, "success", null);
    }
    
    /**
     * 成功返回结果
     */
    public static <T> Result<T> success(T data) {
        return new Result<>(200, "success", data);
    }
    
    /**
     * 成功返回结果
     */
    public static <T> Result<T> success(String message, T data) {
        return new Result<>(200, message, data);
    }
    
    /**
     * 失败返回结果
     */
    public static <T> Result<T> error() {
        return new Result<>(500, "error", null);
    }
    
    /**
     * 失败返回结果
     */
    public static <T> Result<T> error(String message) {
        return new Result<>(500, message, null);
    }
    
    /**
     * 失败返回结果
     */
    public static <T> Result<T> error(Integer code, String message) {
        return new Result<>(code, message, null);
    }
    
    /**
     * 参数错误
     */
    public static <T> Result<T> badRequest(String message) {
        return new Result<>(400, message, null);
    }
    
    /**
     * 未授权
     */
    public static <T> Result<T> unauthorized() {
        return new Result<>(401, "未授权，请先登录", null);
    }
    
    /**
     * 禁止访问
     */
    public static <T> Result<T> forbidden() {
        return new Result<>(403, "禁止访问", null);
    }
    
    /**
     * 资源不存在
     */
    public static <T> Result<T> notFound() {
        return new Result<>(404, "资源不存在", null);
    }
    
    /**
     * 判断是否成功
     */
    public boolean isSuccess() {
        return code != null && code == 200;
    }
}
