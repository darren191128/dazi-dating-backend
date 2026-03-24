package com.dazi.payment.entity;

import com.baomidou.mybatisplus.annotation.*;
import lombok.Data;
import java.time.LocalDateTime;

/**
 * 交易记录实体
 */
@Data
@TableName("transaction_record")
public class TransactionRecord {
    
    @TableId(value = "id", type = IdType.AUTO)
    private Long id;
    
    private Long userId;
    
    /**
     * 类型：1充值 2消费 3赠送 4接收 5系统赠送 6签到奖励
     */
    private Integer type;
    
    /**
     * 货币类型：gold_coin/point
     */
    private String currencyType;
    
    /**
     * 变动金额（正数增加，负数减少）
     */
    private Integer amount;
    
    /**
     * 变动后余额
     */
    private Integer balance;
    
    /**
     * 关联ID（订单ID、礼物ID等）
     */
    private Long relatedId;
    
    /**
     * 交易描述
     */
    private String description;
    
    @TableField(fill = FieldFill.INSERT)
    private LocalDateTime createdAt;
    
    // 交易类型常量
    public static final int TYPE_RECHARGE = 1;
    public static final int TYPE_CONSUME = 2;
    public static final int TYPE_GIVE = 3;
    public static final int TYPE_RECEIVE = 4;
    public static final int TYPE_SYSTEM = 5;
    public static final int TYPE_CHECKIN = 6;
    
    // 货币类型常量
    public static final String CURRENCY_GOLD_COIN = "gold_coin";
    public static final String CURRENCY_POINT = "point";
}
