package com.dazi.vip.dto;

import jakarta.validation.constraints.NotNull;
import lombok.Data;

/**
 * 订阅VIP请求
 */
@Data
public class SubscribeVipRequest {
    
    /**
     * 套餐ID
     */
    @NotNull(message = "套餐ID不能为空")
    private Long packageId;
    
    /**
     * 支付方式：wechat-微信支付
     */
    private String payType = "wechat";
}
