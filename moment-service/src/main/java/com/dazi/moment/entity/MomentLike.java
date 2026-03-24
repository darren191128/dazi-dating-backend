package com.dazi.moment.entity;

import com.baomidou.mybatisplus.annotation.*;
import lombok.Data;
import lombok.EqualsAndHashCode;
import java.time.LocalDateTime;

@Data
@EqualsAndHashCode(callSuper = false)
@TableName("moment_like")
public class MomentLike {
    
    @TableId(value = "id", type = IdType.AUTO)
    private Long id;
    
    private Long momentId;
    
    private Long userId;
    
    @TableField(fill = FieldFill.INSERT)
    private LocalDateTime createdAt;
}
