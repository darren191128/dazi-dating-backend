package com.dazi.vip.dto;

import lombok.Data;

/**
 * VIP特权DTO
 */
@Data
public class VipPrivilegeDTO {
    
    /**
     * 特权编码
     */
    private String code;
    
    /**
     * 特权名称
     */
    private String name;
    
    /**
     * 特权描述
     */
    private String description;
    
    /**
     * 图标URL
     */
    private String icon;
}
