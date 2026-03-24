package com.dazi.common.dto;

import jakarta.validation.constraints.*;
import lombok.Data;

import java.math.BigDecimal;

/**
 * 创建支付订单请求DTO
 */
@Data
public class CreatePaymentDTO {
    
    /**
     * 活动ID
     */
    @NotNull(message = "活动ID不能为空")
    @Positive(message = "活动ID必须为正数")
    private Long activityId;
    
    /**
     * 支付金额
     */
    @NotNull(message = "支付金额不能为空")
    @DecimalMin(value = "0.01", message = "支付金额必须大于0")
    @DecimalMax(value = "999999.99", message = "支付金额超出限制")
    private BigDecimal amount;
    
    /**
     * 支付类型：1-活动费用，2-VIP充值
     */
    @NotNull(message = "支付类型不能为空")
    @Min(value = 1, message = "支付类型无效")
    @Max(value = 2, message = "支付类型无效")
    private Integer type;
}
