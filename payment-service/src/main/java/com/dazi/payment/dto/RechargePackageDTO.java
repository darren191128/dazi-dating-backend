package com.dazi.payment.dto;

import lombok.Data;

/**
 * 充值套餐DTO
 */
@Data
public class RechargePackageDTO {
    
    private Integer id;
    
    private String name;
    
    private Integer price;
    
    private Integer goldCoin;
    
    private Integer bonusCoin;
    
    private String description;
    
    private Boolean isHot;
}
