package com.dazi.admin.entity;

import com.baomidou.mybatisplus.annotation.*;
import lombok.Data;
import java.time.LocalDateTime;

/**
 * 权限实体
 */
@Data
@TableName("admin_permission")
public class AdminPermission {
    
    @TableId(value = "id", type = IdType.AUTO)
    private Long id;
    
    private String permissionCode; // 权限编码：user:view, user:edit等
    
    private String permissionName; // 权限名称
    
    private String module; // 所属模块
    
    private String description; // 描述
    
    @TableField(fill = FieldFill.INSERT)
    private LocalDateTime createTime;
}
