package com.dazi.vip.dto;

import lombok.Data;

import java.time.LocalDateTime;
import java.util.List;

/**
 * 用户VIP状态DTO
 */
@Data
public class UserVipStatusDTO {
    
    /**
     * 是否是VIP
     */
    private Boolean isVip;
    
    /**
     * VIP到期时间
     */
    private LocalDateTime expireTime;
    
    /**
     * 剩余天数
     */
    private Integer remainingDays;
    
    /**
     * 当前套餐名称
     */
    private String packageName;
    
    /**
     * VIP特权列表
     */
    private List<VipPrivilegeDTO> privileges;
}
