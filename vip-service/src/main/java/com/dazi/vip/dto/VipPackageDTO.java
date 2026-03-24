package com.dazi.vip.dto;

import lombok.Data;

import java.math.BigDecimal;
import java.time.LocalDateTime;

/**
 * VIP套餐DTO
 */
@Data
public class VipPackageDTO {
    
    /**
     * 套餐ID
     */
    private Long id;
    
    /**
     * 套餐名称
     */
    private String name;
    
    /**
     * 套餐描述
     */
    private String description;
    
    /**
     * 时长（天）
     */
    private Integer durationDays;
    
    /**
     * 价格
     */
    private BigDecimal price;
    
    /**
     * 原价
     */
    private BigDecimal originalPrice;
    
    /**
     * 是否推荐
     */
    private Boolean isRecommended;
    
    /**
     * 折扣信息
     */
    private String discountInfo;
    
    /**
     * 每日价格
     */
    private BigDecimal dailyPrice;
}
