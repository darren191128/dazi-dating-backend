package com.dazi.vip.service;

import com.dazi.common.result.Result;
import com.dazi.vip.dto.*;
import com.dazi.vip.entity.VipPackage;
import com.dazi.vip.entity.VipPrivilege;

import java.util.List;

/**
 * VIP服务接口
 */
public interface VipService {
    
    /**
     * 获取VIP套餐列表
     */
    List<VipPackageDTO> getPackages();
    
    /**
     * 获取用户VIP状态
     */
    UserVipStatusDTO getVipStatus(Long userId);
    
    /**
     * 订阅VIP
     */
    void subscribe(Long userId, SubscribeVipRequest request);
    
    /**
     * 获取VIP特权列表
     */
    List<VipPrivilege> getPrivileges();
    
    /**
     * 检查用户是否是VIP
     */
    boolean isVip(Long userId);
    
    /**
     * 检查用户是否有特定特权
     */
    boolean hasPrivilege(Long userId, String privilegeCode);
}
