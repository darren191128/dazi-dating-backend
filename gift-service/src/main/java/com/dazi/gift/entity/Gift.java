package com.dazi.gift.entity;

import jakarta.persistence.*;
import lombok.Data;
import org.hibernate.annotations.CreationTimestamp;

import java.time.LocalDateTime;

@Data
@Entity
@Table(name = "gift")
public class Gift {
    
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    
    @Column(name = "category_id", nullable = false)
    private Long categoryId;
    
    @Column(name = "name", nullable = false, length = 64)
    private String name;
    
    @Column(name = "icon", nullable = false, length = 256)
    private String icon;
    
    @Column(name = "animation", length = 256)
    private String animation;
    
    @Column(name = "price", nullable = false)
    private Integer price;
    
    @Column(name = "vip_only")
    private Integer vipOnly = 0;
    
    @Column(name = "description", length = 256)
    private String description;
    
    @Column(name = "sort_order")
    private Integer sortOrder = 0;
    
    @Column(name = "status")
    private Integer status = 1;
    
    @CreationTimestamp
    @Column(name = "created_at", updatable = false)
    private LocalDateTime createdAt;
}
