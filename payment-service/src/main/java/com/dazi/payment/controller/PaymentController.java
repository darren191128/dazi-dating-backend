package com.dazi.payment.controller;

import com.dazi.common.annotation.Log;
import com.dazi.common.dto.CreatePaymentDTO;
import com.dazi.common.result.Result;
import com.dazi.payment.dto.*;
import com.dazi.payment.service.PaymentService;
import com.dazi.payment.service.WalletService;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.validation.Valid;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.Data;
import lombok.RequiredArgsConstructor;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

/**
 * 支付控制器 - 安全增强版
 */
@RestController
@RequestMapping("/api/payment")
@RequiredArgsConstructor
@Validated
public class PaymentController {
    
    private final PaymentService paymentService;
    private final WalletService walletService;
    
    /**
     * 退款请求DTO
     */
    @Data
    public static class RefundDTO {
        @NotBlank(message = "退款原因不能为空")
        @Size(max = 200, message = "退款原因长度不能超过200")
        private String reason;
    }
    
    /**
     * 创建支付订单
     */
    @PostMapping("/order")
    @Log(operation = "创建支付订单", type = "PAYMENT", logParams = true)
    public Result<Map<String, Object>> createOrder(
            HttpServletRequest request,
            @Valid @RequestBody CreatePaymentDTO paymentDTO) {
        
        Long userId = (Long) request.getAttribute("currentUserId");
        return paymentService.createPaymentOrder(userId, paymentDTO.getActivityId(), 
                paymentDTO.getAmount(), paymentDTO.getType());
    }
    
    /**
     * 微信支付回调（带签名验证）
     * 注意：此接口不需要JWT鉴权，但需要签名验证
     */
    @PostMapping("/callback")
    public Result<Void> wxPayCallback(@RequestBody Map<String, String> params) {
        return paymentService.wxPayCallback(params);
    }
    
    /**
     * 查询订单状态（带权限校验）
     */
    @GetMapping("/order/{orderNo}")
    public Result<Map<String, Object>> getOrderStatus(
            HttpServletRequest request,
            @PathVariable String orderNo) {
        
        Long userId = (Long) request.getAttribute("currentUserId");
        return paymentService.getOrderStatus(orderNo, userId);
    }
    
    /**
     * 退款（带权限校验）
     */
    @PostMapping("/order/{orderNo}/refund")
    @Log(operation = "申请退款", type = "PAYMENT", logParams = true)
    public Result<Void> refund(
            HttpServletRequest request,
            @PathVariable @NotBlank(message = "订单号不能为空") String orderNo,
            @Valid @RequestBody RefundDTO refundDTO) {
        
        Long userId = (Long) request.getAttribute("currentUserId");
        return paymentService.refund(orderNo, userId, refundDTO.getReason());
    }
    
    // ==================== 钱包相关接口 ====================
    
    /**
     * 查询钱包余额
     */
    @GetMapping("/wallet")
    public Result<WalletDTO> getWallet(HttpServletRequest request) {
        Long userId = (Long) request.getAttribute("currentUserId");
        return walletService.getWallet(userId);
    }
    
    /**
     * 获取交易记录
     */
    @GetMapping("/wallet/transactions")
    public Result<List<TransactionRecordDTO>> getTransactions(
            HttpServletRequest request,
            @RequestParam(required = false) String currencyType,
            @RequestParam(required = false) Integer type,
            @RequestParam(defaultValue = "1") Integer page,
            @RequestParam(defaultValue = "20") Integer pageSize) {
        
        Long userId = (Long) request.getAttribute("currentUserId");
        return walletService.getTransactions(userId, currencyType, type, page, pageSize);
    }
    
    /**
     * 获取充值套餐列表
     */
    @GetMapping("/wallet/packages")
    public Result<List<RechargePackageDTO>> getRechargePackages() {
        return walletService.getRechargePackages();
    }
    
    /**
     * 充值（创建充值订单）
     */
    @PostMapping("/wallet/recharge")
    @Log(operation = "创建充值订单", type = "PAYMENT", logParams = true)
    public Result<RechargeOrderDTO> createRechargeOrder(
            HttpServletRequest request,
            @Valid @RequestBody RechargeOrderRequest requestBody) {
        
        Long userId = (Long) request.getAttribute("currentUserId");
        return walletService.createRechargeOrder(userId, requestBody.getPackageId());
    }
    
    /**
     * 消费（扣除余额）
     */
    @PostMapping("/wallet/consume")
    @Log(operation = "消费", type = "PAYMENT", logParams = true)
    public Result<Void> consume(
            HttpServletRequest request,
            @Valid @RequestBody ConsumeRequest requestBody) {
        
        Long userId = (Long) request.getAttribute("currentUserId");
        return walletService.consume(userId, requestBody);
    }
}
