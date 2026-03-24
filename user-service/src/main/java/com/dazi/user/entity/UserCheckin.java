package com.dazi.user.entity;

import com.baomidou.mybatisplus.annotation.*;
import lombok.Data;
import java.time.LocalDate;
import java.time.LocalDateTime;

/**
 * 签到记录实体
 */
@Data
@TableName("user_checkin")
public class UserCheckin {
    
    @TableId(value = "id", type = IdType.AUTO)
    private Long id;
    
    private Long userId;
    
    private LocalDate checkinDate;
    
    private Integer consecutiveDays;
    
    private String rewardType;
    
    private Integer rewardAmount;
    
    @TableField(fill = FieldFill.INSERT)
    private LocalDateTime createdAt;
}
