package com.dazi.vip.entity;

import jakarta.persistence.*;
import lombok.Data;
import org.hibernate.annotations.CreationTimestamp;

import java.time.LocalDateTime;

/**
 * VIP特权实体
 */
@Data
@Entity
@Table(name = "vip_privilege", indexes = {
    @Index(name = "uk_code", columnList = "code", unique = true)
})
public class VipPrivilege {
    
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    
    /**
     * 特权编码
     */
    @Column(name = "code", nullable = false, length = 32, unique = true)
    private String code;
    
    /**
     * 特权名称
     */
    @Column(name = "name", nullable = false, length = 64)
    private String name;
    
    /**
     * 特权描述
     */
    @Column(name = "description", length = 256)
    private String description;
    
    /**
     * 图标URL
     */
    @Column(name = "icon", length = 256)
    private String icon;
    
    /**
     * 排序
     */
    @Column(name = "sort_order")
    private Integer sortOrder = 0;
    
    /**
     * 状态
     */
    @Column(name = "status")
    private Integer status = 1;
    
    /**
     * 创建时间
     */
    @CreationTimestamp
    @Column(name = "created_at", updatable = false)
    private LocalDateTime createdAt;
}
