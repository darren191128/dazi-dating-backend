package com.dazi.admin.entity;

import com.baomidou.mybatisplus.annotation.*;
import lombok.Data;
import java.time.LocalDate;
import java.time.LocalDateTime;

/**
 * 财务报表实体（日统计）
 */
@Data
@TableName("finance_daily_report")
public class FinanceDailyReport {
    
    @TableId(value = "id", type = IdType.AUTO)
    private Long id;
    
    private LocalDate reportDate; // 报表日期
    
    private Integer totalOrders; // 总订单数
    
    private Integer successOrders; // 成功订单数
    
    private Integer refundOrders; // 退款订单数
    
    private java.math.BigDecimal totalIncome; // 总收入
    
    private java.math.BigDecimal totalRefund; // 总退款
    
    private java.math.BigDecimal netIncome; // 净收入
    
    private Integer newUsers; // 新增用户数
    
    private Integer activeUsers; // 活跃用户数
    
    private Integer newActivities; // 新增活动数
    
    private Integer totalParticipants; // 总参与人次
    
    @TableField(fill = FieldFill.INSERT)
    private LocalDateTime createTime;
    
    @TableField(fill = FieldFill.INSERT_UPDATE)
    private LocalDateTime updateTime;
}
