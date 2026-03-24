package com.dazi.common.dto;

import jakarta.validation.constraints.*;
import lombok.Data;

/**
 * 用户资料更新请求DTO
 */
@Data
public class UpdateProfileDTO {
    
    /**
     * 昵称
     */
    @Size(max = 50, message = "昵称长度不能超过50")
    @Pattern(regexp = "^[^<>\"'&]*$", message = "昵称包含非法字符")
    private String nickname;
    
    /**
     * 个人简介
     */
    @Size(max = 500, message = "简介长度不能超过500")
    @Pattern(regexp = "^[^<>\"'&]*$", message = "简介包含非法字符")
    private String bio;
    
    /**
     * 年龄
     */
    @Min(value = 18, message = "年龄必须大于等于18")
    @Max(value = 100, message = "年龄必须小于等于100")
    private Integer age;
}
