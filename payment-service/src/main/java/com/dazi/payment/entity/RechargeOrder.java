package com.dazi.payment.entity;

import com.baomidou.mybatisplus.annotation.*;
import lombok.Data;
import java.math.BigDecimal;
import java.time.LocalDateTime;

/**
 * 充值订单实体
 */
@Data
@TableName("recharge_order")
public class RechargeOrder {
    
    @TableId(value = "id", type = IdType.AUTO)
    private Long id;
    
    private String orderNo;
    
    private Long userId;
    
    private BigDecimal amount;
    
    private Integer goldCoin;
    
    private Integer bonusCoin;
    
    /**
     * 状态：0待支付 1已支付 2已取消
     */
    private Integer status;
    
    private String payType;
    
    private String wxPayTradeNo;
    
    private LocalDateTime payTime;
    
    @TableField(fill = FieldFill.INSERT)
    private LocalDateTime createdAt;
    
    @TableField(fill = FieldFill.INSERT_UPDATE)
    private LocalDateTime updatedAt;
    
    // 状态常量
    public static final int STATUS_PENDING = 0;
    public static final int STATUS_PAID = 1;
    public static final int STATUS_CANCELLED = 2;
}
