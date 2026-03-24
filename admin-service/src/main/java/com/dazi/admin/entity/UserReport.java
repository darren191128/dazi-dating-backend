package com.dazi.admin.entity;

import com.baomidou.mybatisplus.annotation.*;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.time.LocalDateTime;

@Data
@EqualsAndHashCode(callSuper = false)
@TableName("user_report")
public class UserReport {
    
    @TableId(value = "id", type = IdType.AUTO)
    private Long id;
    
    private Long reporterId;
    
    private Long reportedId;
    
    private Integer type;
    
    private String reason;
    
    private String evidence;
    
    private Integer status;
    
    private String handleResult;
    
    private Long handlerId;
    
    private LocalDateTime handledAt;
    
    @TableField(fill = FieldFill.INSERT)
    private LocalDateTime createdAt;
}
