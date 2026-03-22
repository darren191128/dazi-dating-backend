package com.dazi.review.entity;

import com.baomidou.mybatisplus.annotation.*;
import lombok.Data;
import java.time.LocalDateTime;

@Data
@TableName("review")
public class Review {
    
    @TableId(value = "id", type = IdType.AUTO)
    private Long id;
    
    private Long activityId;
    
    private Long reviewerId;
    
    private Long revieweeId;
    
    private Integer rating;
    
    private String content;
    
    private String tags;
    
    @TableField(fill = FieldFill.INSERT)
    private LocalDateTime createTime;
    
    @TableField(fill = FieldFill.INSERT_UPDATE)
    private LocalDateTime updateTime;
}
