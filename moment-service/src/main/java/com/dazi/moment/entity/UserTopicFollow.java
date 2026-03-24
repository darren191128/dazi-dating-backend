package com.dazi.moment.entity;

import com.baomidou.mybatisplus.annotation.*;
import lombok.Data;
import java.time.LocalDateTime;

@Data
@TableName("user_topic_follow")
public class UserTopicFollow {
    
    @TableId(value = "id", type = IdType.AUTO)
    private Long id;
    
    private Long userId;
    
    private Long topicId;
    
    @TableField(fill = FieldFill.INSERT)
    private LocalDateTime createdAt;
}
