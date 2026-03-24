package com.dazi.admin.entity;

import com.baomidou.mybatisplus.annotation.*;
import lombok.Data;
import java.time.LocalDateTime;

/**
 * 角色权限关联实体
 */
@Data
@TableName("admin_role_permission")
public class AdminRolePermission {
    
    @TableId(value = "id", type = IdType.AUTO)
    private Long id;
    
    private Integer role; // 角色：1超级管理员 2普通管理员 3运营
    
    private Long permissionId; // 权限ID
    
    @TableField(fill = FieldFill.INSERT)
    private LocalDateTime createTime;
}
