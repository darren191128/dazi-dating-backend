package com.dazi.moment.entity;

import com.baomidou.mybatisplus.annotation.*;
import lombok.Data;
import java.time.LocalDateTime;

@Data
@TableName("moment_topic")
public class MomentTopic {
    
    @TableId(value = "id", type = IdType.AUTO)
    private Long id;
    
    private Long momentId;
    
    private Long topicId;
    
    @TableField(fill = FieldFill.INSERT)
    private LocalDateTime createdAt;
}
