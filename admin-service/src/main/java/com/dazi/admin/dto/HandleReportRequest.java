package com.dazi.admin.dto;

import jakarta.validation.constraints.NotNull;
import lombok.Data;

@Data
public class HandleReportRequest {
    
    @NotNull(message = "处理结果不能为空")
    private Integer status;
    
    private String handleResult;
}
