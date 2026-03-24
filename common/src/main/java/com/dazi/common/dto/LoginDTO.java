package com.dazi.common.dto;

import jakarta.validation.constraints.*;
import lombok.Data;

/**
 * 用户登录请求DTO
 */
@Data
public class LoginDTO {
    
    /**
     * 微信登录code
     */
    @NotBlank(message = "微信授权code不能为空")
    @Size(max = 100, message = "code长度不能超过100")
    private String code;
    
    /**
     * 昵称
     */
    @Size(max = 50, message = "昵称长度不能超过50")
    private String nickname;
    
    /**
     * 头像URL
     */
    @Size(max = 500, message = "头像URL长度不能超过500")
    private String avatar;
    
    /**
     * 性别：0-未知，1-男，2-女
     */
    @Min(value = 0, message = "性别取值范围为0-2")
    @Max(value = 2, message = "性别取值范围为0-2")
    private Integer gender;
}
