package com.dazi.common.dto;

import jakarta.validation.constraints.*;
import lombok.Data;

/**
 * 分页查询基础DTO
 */
@Data
public class PageQueryDTO {
    
    /**
     * 页码
     */
    @Min(value = 1, message = "页码必须大于等于1")
    private Integer page = 1;
    
    /**
     * 每页大小
     */
    @Min(value = 1, message = "每页大小必须大于等于1")
    @Max(value = 100, message = "每页大小不能超过100")
    private Integer pageSize = 20;
}
