package com.dazi.admin.entity;

import com.baomidou.mybatisplus.annotation.*;
import lombok.Data;
import java.time.LocalDateTime;

/**
 * 系统配置实体
 */
@Data
@TableName("system_config")
public class SystemConfig {
    
    @TableId(value = "id", type = IdType.AUTO)
    private Long id;
    
    private String configKey;
    
    private String configValue;
    
    private String description;
    
    private String category; // 分类：payment、user、activity等
    
    @TableField(fill = FieldFill.INSERT)
    private LocalDateTime createTime;
    
    @TableField(fill = FieldFill.INSERT_UPDATE)
    private LocalDateTime updateTime;
}
