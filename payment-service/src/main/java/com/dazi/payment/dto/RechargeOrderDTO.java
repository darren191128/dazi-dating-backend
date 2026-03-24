package com.dazi.payment.dto;

import lombok.Data;
import java.math.BigDecimal;
import java.time.LocalDateTime;

/**
 * 充值订单DTO
 */
@Data
public class RechargeOrderDTO {
    
    private String orderNo;
    
    private BigDecimal amount;
    
    private Integer goldCoin;
    
    private Integer bonusCoin;
    
    private Integer status;
    
    private String statusName;
    
    private LocalDateTime payTime;
    
    private LocalDateTime createdAt;
}
