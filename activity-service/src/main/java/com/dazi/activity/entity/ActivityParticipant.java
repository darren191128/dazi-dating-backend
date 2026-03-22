package com.dazi.activity.entity;

import com.baomidou.mybatisplus.annotation.*;
import lombok.Data;
import java.time.LocalDateTime;

@Data
@TableName("activity_participant")
public class ActivityParticipant {
    
    @TableId(value = "id", type = IdType.AUTO)
    private Long id;
    
    private Long activityId;
    
    private Long userId;
    
    private Integer status;
    
    private LocalDateTime joinTime;
    
    private LocalDateTime quitTime;
    
    @TableField(fill = FieldFill.INSERT)
    private LocalDateTime createTime;
    
    @TableField(fill = FieldFill.INSERT_UPDATE)
    private LocalDateTime updateTime;
}
