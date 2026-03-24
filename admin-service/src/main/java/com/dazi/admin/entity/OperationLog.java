package com.dazi.admin.entity;

import com.baomidou.mybatisplus.annotation.*;
import lombok.Data;
import java.time.LocalDateTime;

/**
 * 操作日志实体
 */
@Data
@TableName("operation_log")
public class OperationLog {
    
    @TableId(value = "id", type = IdType.AUTO)
    private Long id;
    
    private Long adminId;
    
    private String adminName;
    
    private String operation; // 操作类型
    
    private String module; // 操作模块
    
    private String description; // 操作描述
    
    private String requestData; // 请求数据
    
    private String responseData; // 响应数据
    
    private String ip; // 操作IP
    
    private Integer status; // 0失败 1成功
    
    private Long executionTime; // 执行时长(ms)
    
    @TableField(fill = FieldFill.INSERT)
    private LocalDateTime createTime;
}
