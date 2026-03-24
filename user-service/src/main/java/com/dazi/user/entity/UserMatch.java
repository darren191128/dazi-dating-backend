package com.dazi.user.entity;

import com.baomidou.mybatisplus.annotation.*;
import lombok.Data;
import lombok.EqualsAndHashCode;
import java.time.LocalDateTime;

@Data
@EqualsAndHashCode(callSuper = false)
@TableName("user_match")
public class UserMatch {
    
    @TableId(value = "id", type = IdType.AUTO)
    private Long id;
    
    private Long userId;
    
    private Long targetUserId;
    
    /**
     * 1-喜欢, 2-不喜欢
     */
    private Integer action;
    
    /**
     * 是否互相喜欢
     */
    private Integer isMutual;
    
    @TableField(fill = FieldFill.INSERT)
    private LocalDateTime createdAt;
}
