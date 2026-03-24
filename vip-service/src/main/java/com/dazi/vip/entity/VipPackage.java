package com.dazi.vip.entity;

import jakarta.persistence.*;
import lombok.Data;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

import java.math.BigDecimal;
import java.time.LocalDateTime;

/**
 * VIP套餐实体
 */
@Data
@Entity
@Table(name = "vip_package")
public class VipPackage {
    
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    
    /**
     * 套餐名称
     */
    @Column(name = "name", nullable = false, length = 64)
    private String name;
    
    /**
     * 套餐描述
     */
    @Column(name = "description", length = 256)
    private String description;
    
    /**
     * 时长（天）
     */
    @Column(name = "duration_days", nullable = false)
    private Integer durationDays;
    
    /**
     * 价格
     */
    @Column(name = "price", nullable = false, precision = 10, scale = 2)
    private BigDecimal price;
    
    /**
     * 原价
     */
    @Column(name = "original_price", precision = 10, scale = 2)
    private BigDecimal originalPrice;
    
    /**
     * 排序
     */
    @Column(name = "sort_order")
    private Integer sortOrder = 0;
    
    /**
     * 是否推荐
     */
    @Column(name = "is_recommended")
    private Boolean isRecommended = false;
    
    /**
     * 状态：0下架 1上架
     */
    @Column(name = "status")
    private Integer status = 1;
    
    /**
     * 创建时间
     */
    @CreationTimestamp
    @Column(name = "created_at", updatable = false)
    private LocalDateTime createdAt;
    
    /**
     * 更新时间
     */
    @UpdateTimestamp
    @Column(name = "updated_at")
    private LocalDateTime updatedAt;
}
