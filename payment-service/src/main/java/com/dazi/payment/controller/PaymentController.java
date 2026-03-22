package com.dazi.payment.controller;

import com.dazi.common.result.Result;
import com.dazi.payment.service.PaymentService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.math.BigDecimal;
import java.util.Map;

@RestController
@RequestMapping("/api/payment")
@RequiredArgsConstructor
public class PaymentController {
    
    private final PaymentService paymentService;
    
    /**
     * 创建支付订单
     */
    @PostMapping("/create")
    public Result<Map<String, Object>> createPaymentOrder(
            @RequestParam Long userId,
            @RequestParam Long activityId,
            @RequestParam BigDecimal amount,
            @RequestParam Integer type) {
        return paymentService.createPaymentOrder(userId, activityId, amount, type);
    }
    
    /**
     * 微信支付回调
     */
    @PostMapping("/callback")
    public Result<Void> wxPayCallback(
            @RequestParam String orderNo,
            @RequestParam String wxPayTradeNo,
            @RequestParam String status) {
        return paymentService.wxPayCallback(orderNo, wxPayTradeNo, status);
    }
    
    /**
     * 退款
     */
    @PostMapping("/refund")
    public Result<Void> refund(
            @RequestParam String orderNo,
            @RequestParam String reason) {
        return paymentService.refund(orderNo, reason);
    }
    
    /**
     * 查询订单状态
     */
    @GetMapping("/status/{orderNo}")
    public Result<Map<String, Object>> getOrderStatus(@PathVariable String orderNo) {
        return paymentService.getOrderStatus(orderNo);
    }
}
