package com.dazi.common.dto;

import jakarta.validation.constraints.*;
import lombok.Data;

import java.math.BigDecimal;
import java.time.LocalDateTime;

/**
 * 创建活动请求DTO
 */
@Data
public class CreateActivityDTO {
    
    /**
     * 活动标题
     */
    @NotBlank(message = "活动标题不能为空")
    @Size(max = 100, message = "标题长度不能超过100")
    @Pattern(regexp = "^[^<>\"'&]*$", message = "标题包含非法字符")
    private String title;
    
    /**
     * 活动描述
     */
    @Size(max = 2000, message = "描述长度不能超过2000")
    @Pattern(regexp = "^[^<>\"'&]*$", message = "描述包含非法字符")
    private String description;
    
    /**
     * 活动类型
     */
    @NotNull(message = "活动类型不能为空")
    @Min(value = 1, message = "活动类型无效")
    private Integer type;
    
    /**
     * 活动开始时间
     */
    @NotNull(message = "活动开始时间不能为空")
    @Future(message = "活动开始时间必须是未来时间")
    private LocalDateTime startTime;
    
    /**
     * 活动结束时间
     */
    @Future(message = "活动结束时间必须是未来时间")
    private LocalDateTime endTime;
    
    /**
     * 活动地点
     */
    @NotBlank(message = "活动地点不能为空")
    @Size(max = 200, message = "地点长度不能超过200")
    private String location;
    
    /**
     * 经度
     */
    @DecimalMin(value = "-180.0", message = "经度范围错误")
    @DecimalMax(value = "180.0", message = "经度范围错误")
    private BigDecimal longitude;
    
    /**
     * 纬度
     */
    @DecimalMin(value = "-90.0", message = "纬度范围错误")
    @DecimalMax(value = "90.0", message = "纬度范围错误")
    private BigDecimal latitude;
    
    /**
     * 最小参与人数
     */
    @Min(value = 2, message = "最小参与人数至少为2")
    private Integer minParticipants;
    
    /**
     * 最大参与人数
     */
    @Min(value = 2, message = "最大参与人数至少为2")
    @Max(value = 1000, message = "最大参与人数不能超过1000")
    private Integer maxParticipants;
    
    /**
     * 支付方式：1-AA制，2-男A女免，3-请客，4-免费
     */
    @NotNull(message = "支付方式不能为空")
    @Min(value = 1, message = "支付方式无效")
    @Max(value = 4, message = "支付方式无效")
    private Integer paymentType;
    
    /**
     * 人均费用
     */
    @DecimalMin(value = "0.00", message = "费用不能为负数")
    @DecimalMax(value = "999999.99", message = "费用超出限制")
    private BigDecimal perPersonAmount;
    
    /**
     * 封面图片URL
     */
    @Size(max = 500, message = "图片URL长度不能超过500")
    @Pattern(regexp = "^https?://.*$", message = "图片URL格式错误")
    private String coverImage;
    
    /**
     * 报名截止时间
     */
    @Future(message = "报名截止时间必须是未来时间")
    private LocalDateTime registrationDeadline;
}
