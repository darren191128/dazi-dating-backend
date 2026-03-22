package com.dazi.activity.entity;

import com.baomidou.mybatisplus.annotation.*;
import lombok.Data;
import java.math.BigDecimal;
import java.time.LocalDateTime;

@Data
@TableName("activity")
public class Activity {
    
    @TableId(value = "id", type = IdType.AUTO)
    private Long id;
    
    private Long userId;
    
    private String title;
    
    private String description;
    
    private Integer type;
    
    private String typeName;
    
    private LocalDateTime startTime;
    
    private LocalDateTime endTime;
    
    private String location;
    
    private BigDecimal longitude;
    
    private BigDecimal latitude;
    
    private Integer minParticipants;
    
    private Integer maxParticipants;
    
    private Integer currentParticipants;
    
    private Integer paymentType;
    
    private BigDecimal totalAmount;
    
    private BigDecimal perPersonAmount;
    
    private String coverImage;
    
    private LocalDateTime registrationDeadline;
    
    private Integer status;
    
    @TableField(fill = FieldFill.INSERT)
    private LocalDateTime createTime;
    
    @TableField(fill = FieldFill.INSERT_UPDATE)
    private LocalDateTime updateTime;
    
    @TableLogic
    @TableField(fill = FieldFill.INSERT)
    private Integer deleted;
}
