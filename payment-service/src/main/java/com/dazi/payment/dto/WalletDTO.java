package com.dazi.payment.dto;

import lombok.Data;
import java.math.BigDecimal;

/**
 * 钱包DTO
 */
@Data
public class WalletDTO {
    
    private Long userId;
    
    private Integer goldCoin;
    
    private Integer point;
    
    private BigDecimal totalRecharge;
    
    private BigDecimal totalConsume;
}
