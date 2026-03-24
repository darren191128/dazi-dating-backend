package com.dazi.vip.entity;

import jakarta.persistence.*;
import lombok.Data;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

import java.time.LocalDateTime;

/**
 * 用户VIP记录实体
 */
@Data
@Entity
@Table(name = "user_vip", indexes = {
    @Index(name = "idx_end_time", columnList = "end_time")
})
public class UserVip {
    
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    
    /**
     * 用户ID
     */
    @Column(name = "user_id", nullable = false, unique = true)
    private Long userId;
    
    /**
     * 套餐ID
     */
    @Column(name = "package_id", nullable = false)
    private Long packageId;
    
    /**
     * 开始时间
     */
    @Column(name = "start_time", nullable = false)
    private LocalDateTime startTime;
    
    /**
     * 结束时间
     */
    @Column(name = "end_time", nullable = false)
    private LocalDateTime endTime;
    
    /**
     * 状态：0过期 1有效
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
    
    /**
     * 判断是否有效
     */
    public boolean isValid() {
        return status == 1 && endTime != null && endTime.isAfter(LocalDateTime.now());
    }
}
