package com.dazi.moment.entity;

import com.baomidou.mybatisplus.annotation.*;
import lombok.Data;
import lombok.EqualsAndHashCode;
import java.time.LocalDateTime;

@Data
@EqualsAndHashCode(callSuper = false)
@TableName("moment_comment")
public class MomentComment {
    
    @TableId(value = "id", type = IdType.AUTO)
    private Long id;
    
    private Long momentId;
    
    private Long userId;
    
    private Long parentId;
    
    private String content;
    
    private Integer likeCount;
    
    private Integer status;
    
    @TableField(fill = FieldFill.INSERT)
    private LocalDateTime createdAt;
    
    @TableLogic
    @TableField(fill = FieldFill.INSERT)
    private Integer deleted;
}
