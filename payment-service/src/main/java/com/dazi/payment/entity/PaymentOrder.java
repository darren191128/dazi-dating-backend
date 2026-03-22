package com.dazi.payment.entity;

import com.baomidou.mybatisplus.annotation.*;
import lombok.Data;
import java.math.BigDecimal;
import java.time.LocalDateTime;

@Data
@TableName("payment_order")
public class PaymentOrder {
    
    @TableId(value = "id", type = IdType.AUTO)
    private Long id;
    
    private String orderNo;
    
    private Long userId;
    
    private Long activityId;
    
    private BigDecimal amount;
    
    private Integer type;
    
    private Integer status;
    
    private String wxPayTradeNo;
    
    private LocalDateTime payTime;
    
    private LocalDateTime refundTime;
    
    private String refundReason;
    
    @TableField(fill = FieldFill.INSERT)
    private LocalDateTime createTime;
    
    @TableField(fill = FieldFill.INSERT_UPDATE)
    private LocalDateTime updateTime;
    
    @TableLogic
    @TableField(fill = FieldFill.INSERT)
    private Integer deleted;
}
