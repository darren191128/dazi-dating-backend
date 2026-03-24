package com.dazi.user.entity;

import com.baomidou.mybatisplus.annotation.*;
import lombok.Data;
import java.time.LocalDate;
import java.time.LocalDateTime;

/**
 * 用户签到统计实体
 */
@Data
@TableName("user_checkin_stats")
public class UserCheckinStats {
    
    @TableId(value = "id", type = IdType.AUTO)
    private Long id;
    
    private Long userId;
    
    private Integer totalCheckinDays;
    
    private Integer maxConsecutiveDays;
    
    private Integer currentConsecutiveDays;
    
    private LocalDate lastCheckinDate;
    
    @TableField(fill = FieldFill.INSERT_UPDATE)
    private LocalDateTime updatedAt;
}
