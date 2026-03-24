package com.dazi.admin.entity;

import com.baomidou.mybatisplus.annotation.*;
import lombok.Data;
import java.time.LocalDateTime;

/**
 * 管理员实体
 */
@Data
@TableName("admin_user")
public class AdminUser {
    
    @TableId(value = "id", type = IdType.AUTO)
    private Long id;
    
    private String username;
    
    private String password;
    
    private String realName;
    
    private String phone;
    
    private String email;
    
    private String avatar;
    
    private Integer role; // 1超级管理员 2普通管理员 3运营
    
    private Integer status; // 0禁用 1启用
    
    private LocalDateTime lastLoginTime;
    
    private String lastLoginIp;
    
    @TableField(fill = FieldFill.INSERT)
    private LocalDateTime createTime;
    
    @TableField(fill = FieldFill.INSERT_UPDATE)
    private LocalDateTime updateTime;
    
    @TableLogic
    @TableField(fill = FieldFill.INSERT)
    private Integer deleted;
}
