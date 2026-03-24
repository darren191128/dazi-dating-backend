package com.dazi.common.dto;

import jakarta.validation.constraints.*;
import lombok.Data;

/**
 * 关注/取消关注请求DTO
 */
@Data
public class FollowDTO {
    
    /**
     * 目标用户ID
     */
    @NotNull(message = "目标用户ID不能为空")
    @Positive(message = "目标用户ID必须为正数")
    private Long followUserId;
}
