package com.dazi.payment.dto;

import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Data;

/**
 * 消费请求DTO
 */
@Data
public class ConsumeRequest {
    
    @NotBlank(message = "货币类型不能为空")
    private String currencyType;
    
    @NotNull(message = "消费金额不能为空")
    @Min(value = 1, message = "消费金额必须大于0")
    private Integer amount;
    
    private String description;
    
    private Long relatedId;
}
