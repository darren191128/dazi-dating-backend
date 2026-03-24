package com.dazi.payment.dto;

import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;
import lombok.Data;

/**
 * 充值订单请求DTO
 */
@Data
public class RechargeOrderRequest {
    
    @NotNull(message = "充值套餐ID不能为空")
    @Min(value = 1, message = "充值套餐ID必须大于0")
    private Integer packageId;
}
