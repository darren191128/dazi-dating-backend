package com.dazi.payment.entity;

import com.baomidou.mybatisplus.annotation.*;
import lombok.Data;
import java.math.BigDecimal;
import java.time.LocalDateTime;

/**
 * 用户钱包实体
 */
@Data
@TableName("user_wallet")
public class UserWallet {
    
    @TableId(value = "id", type = IdType.AUTO)
    private Long id;
    
    private Long userId;
    
    private Integer goldCoin;
    
    private Integer point;
    
    private BigDecimal totalRecharge;
    
    private BigDecimal totalConsume;
    
    @TableField(fill = FieldFill.INSERT)
    private LocalDateTime createdAt;
    
    @TableField(fill = FieldFill.INSERT_UPDATE)
    private LocalDateTime updatedAt;
}
