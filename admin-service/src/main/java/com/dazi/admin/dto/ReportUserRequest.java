package com.dazi.admin.dto;

import jakarta.validation.constraints.NotNull;
import lombok.Data;

import java.util.List;

@Data
public class ReportUserRequest {
    
    @NotNull(message = "被举报人ID不能为空")
    private Long reportedId;
    
    @NotNull(message = "举报类型不能为空")
    private Integer type;
    
    private String reason;
    
    private List<String> evidence;
}
