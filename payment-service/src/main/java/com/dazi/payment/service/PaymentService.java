package com.dazi.payment.service;

import com.dazi.payment.entity.PaymentOrder;
import com.dazi.payment.repository.PaymentOrderRepository;
import com.dazi.common.result.Result;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

@Slf4j
@Service
@RequiredArgsConstructor
public class PaymentService {
    
    private final PaymentOrderRepository paymentOrderRepository;
    
    /**
     * 创建支付订单
     */
    @Transactional
    public Result<Map<String, Object>> createPaymentOrder(Long userId, Long activityId, BigDecimal amount, Integer type) {
        // 生成订单号
        String orderNo = generateOrderNo();
        
        PaymentOrder order = new PaymentOrder();
        order.setOrderNo(orderNo);
        order.setUserId(userId);
        order.setActivityId(activityId);
        order.setAmount(amount);
        order.setType(type);
        order.setStatus(0); // 待支付
        
        paymentOrderRepository.insert(order);
        
        log.info("创建支付订单: orderNo={}, userId={}, amount={}", orderNo, userId, amount);
        
        // 模拟调用微信支付
        Map<String, Object> result = new HashMap<>();
        result.put("orderNo", orderNo);
        result.put("amount", amount);
        result.put("status", "pending");
        
        return Result.success(result);
    }
    
    /**
     * 微信支付回调
     */
    @Transactional
    public Result<Void> wxPayCallback(String orderNo, String wxPayTradeNo, String status) {
        PaymentOrder order = paymentOrderRepository.findByOrderNo(orderNo);
        if (order == null) {
            return Result.error("订单不存在");
        }
        
        if ("SUCCESS".equals(status)) {
            order.setStatus(1); // 已支付
            order.setWxPayTradeNo(wxPayTradeNo);
            order.setPayTime(LocalDateTime.now());
            paymentOrderRepository.updateById(order);
            
            log.info("支付成功: orderNo={}, wxPayTradeNo={}", orderNo, wxPayTradeNo);
        } else {
            order.setStatus(2); // 支付失败
            paymentOrderRepository.updateById(order);
            
            log.warn("支付失败: orderNo={}, status={}", orderNo, status);
        }
        
        return Result.success();
    }
    
    /**
     * 退款
     */
    @Transactional
    public Result<Void> refund(String orderNo, String reason) {
        PaymentOrder order = paymentOrderRepository.findByOrderNo(orderNo);
        if (order == null) {
            return Result.error("订单不存在");
        }
        
        if (order.getStatus() != 1) {
            return Result.error("订单未支付，无法退款");
        }
        
        order.setStatus(3); // 已退款
        order.setRefundReason(reason);
        order.setRefundTime(LocalDateTime.now());
        paymentOrderRepository.updateById(order);
        
        log.info("退款成功: orderNo={}, amount={}, reason={}", orderNo, order.getAmount(), reason);
        
        return Result.success();
    }
    
    /**
     * 查询订单状态
     */
    public Result<Map<String, Object>> getOrderStatus(String orderNo) {
        PaymentOrder order = paymentOrderRepository.findByOrderNo(orderNo);
        if (order == null) {
            return Result.error("订单不存在");
        }
        
        Map<String, Object> result = new HashMap<>();
        result.put("orderNo", order.getOrderNo());
        result.put("amount", order.getAmount());
        result.put("status", order.getStatus());
        result.put("payTime", order.getPayTime());
        result.put("refundTime", order.getRefundTime());
        
        return Result.success(result);
    }
    
    /**
     * 生成订单号
     */
    private String generateOrderNo() {
        String dateStr = LocalDateTime.now().format(DateTimeFormatter.ofPattern("yyyyMMddHHmmss"));
        String uuid = UUID.randomUUID().toString().substring(0, 8);
        return "DZ" + dateStr + uuid;
    }
}
