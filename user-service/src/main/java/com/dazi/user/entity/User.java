package com.dazi.user.entity;

import com.baomidou.mybatisplus.annotation.*;
import lombok.Data;
import lombok.EqualsAndHashCode;
import java.time.LocalDateTime;

@Data
@EqualsAndHashCode(callSuper = false)
@TableName("user")
public class User {
    
    @TableId(value = "id", type = IdType.AUTO)
    private Long id;
    
    private String openId;
    
    private String unionId;
    
    private String nickname;
    
    private String avatar;
    
    private Integer gender;
    
    private Integer age;
    
    private String phone;
    
    private Integer level;
    
    private Integer exp;
    
    private Integer creditScore;
    
    private Integer status;
    
    private LocalDateTime lastLoginTime;
    
    @TableField(fill = FieldFill.INSERT)
    private LocalDateTime createTime;
    
    @TableField(fill = FieldFill.INSERT_UPDATE)
    private LocalDateTime updateTime;
    
    @TableLogic
    @TableField(fill = FieldFill.INSERT)
    private Integer deleted;
}
