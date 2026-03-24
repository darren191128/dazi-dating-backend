package com.dazi.admin.entity;

import com.baomidou.mybatisplus.annotation.*;
import lombok.Data;
import java.math.BigDecimal;
import java.time.LocalDateTime;

/**
 * 财务流水实体
 */
@Data
@TableName("finance_record")
public class FinanceRecord {
    
    @TableId(value = "id", type = IdType.AUTO)
    private Long id;
    
    private String orderNo; // 关联订单号
    
    private Long userId; // 用户ID
    
    private Integer type; // 1收入 2支出 3退款
    
    private BigDecimal amount; // 金额
    
    private BigDecimal balance; // 变动后余额
    
    private String description; // 描述
    
    private String remark; // 备注
    
    private Long relatedId; // 关联业务ID
    
    private String relatedType; // 关联业务类型：activity/payment/refund
    
    @TableField(fill = FieldFill.INSERT)
    private LocalDateTime createTime;
}
