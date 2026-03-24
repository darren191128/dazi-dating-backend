package com.dazi.payment.service;

import com.dazi.payment.entity.PaymentOrder;
import com.dazi.payment.repository.PaymentOrderRepository;
import com.dazi.common.result.Result;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.nio.charset.StandardCharsets;
import java.security.MessageDigest;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Formatter;
import java.util.HashMap;
import java.util.Map;
import java.util.UUID;
import java.util.concurrent.TimeUnit;

/**
 * 支付服务 - 安全增强版
 */
@Slf4j
@Service
@RequiredArgsConstructor
public class PaymentService {
    
    private final PaymentOrderRepository paymentOrderRepository;
    private final RedisTemplate<String, Object> redisTemplate;
    
    @Value("${wxpay.app-id}")
    private String wxAppId;
    
    @Value("${wxpay.mch-id}")
    private String wxMchId;
    
    @Value("${wxpay.api-key}")
    private String wxApiKey;
    
    private static final String ORDER_IDEMPOTENCY_PREFIX = "payment:idempotency:";
    private static final String CALLBACK_NONCE_PREFIX = "payment:callback:nonce:";
    
    /**
     * 创建支付订单（带幂等性校验）
     */
    @Transactional
    public Result<Map<String, Object>> createPaymentOrder(Long userId, Long activityId, BigDecimal amount, Integer type) {
        // 幂等性校验：防止重复创建订单
        String idempotencyKey = ORDER_IDEMPOTENCY_PREFIX + userId + ":" + activityId + ":" + type;
        
        // 检查是否已有进行中的订单
        String existingOrder = (String) redisTemplate.opsForValue().get(idempotencyKey);
        if (existingOrder != null) {
            PaymentOrder order = paymentOrderRepository.findByOrderNo(existingOrder);
            if (order != null && order.getStatus() == 0) {
                log.info("返回已有待支付订单: orderNo={}, userId={}", existingOrder, userId);
                Map<String, Object> result = new HashMap<>();
                result.put("orderNo", order.getOrderNo());
                result.put("amount", order.getAmount());
                result.put("status", "pending");
                return Result.success(result);
            }
        }
        
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
        
        // 设置幂等性缓存（5分钟）
        redisTemplate.opsForValue().set(idempotencyKey, orderNo, 5, TimeUnit.MINUTES);
        
        log.info("创建支付订单: orderNo={}, userId={}, amount={}", orderNo, userId, amount);
        
        Map<String, Object> result = new HashMap<>();
        result.put("orderNo", orderNo);
        result.put("amount", amount);
        result.put("status", "pending");
        
        return Result.success(result);
    }
    
    /**
     * 微信支付回调（带签名验证和幂等性校验）
     */
    @Transactional
    public Result<Void> wxPayCallback(Map<String, String> params) {
        String orderNo = params.get("orderNo");
        String wxPayTradeNo = params.get("wxPayTradeNo");
        String status = params.get("status");
        String nonceStr = params.get("nonceStr");
        String timestamp = params.get("timestamp");
        String signature = params.get("signature");
        
        // 1. 参数校验
        if (orderNo == null || wxPayTradeNo == null || status == null || signature == null) {
            log.error("支付回调参数缺失");
            return Result.error("参数缺失");
        }
        
        // 2. 签名验证
        if (!verifyCallbackSignature(params)) {
            log.error("支付回调签名验证失败: orderNo={}", orderNo);
            return Result.error("签名验证失败");
        }
        
        // 3. 防重放攻击：检查nonce是否已使用
        String nonceKey = CALLBACK_NONCE_PREFIX + nonceStr;
        if (Boolean.TRUE.equals(redisTemplate.hasKey(nonceKey))) {
            log.warn("支付回调重复请求: nonceStr={}", nonceStr);
            return Result.success(); // 返回成功，但不处理
        }
        
        // 记录nonce（5分钟过期）
        redisTemplate.opsForValue().set(nonceKey, "1", 5, TimeUnit.MINUTES);
        
        // 4. 查询订单
        PaymentOrder order = paymentOrderRepository.findByOrderNo(orderNo);
        if (order == null) {
            log.error("支付回调订单不存在: orderNo={}", orderNo);
            return Result.error("订单不存在");
        }
        
        // 5. 幂等性校验：已处理过的订单不再处理
        if (order.getStatus() == 1 && "SUCCESS".equals(status)) {
            log.info("订单已支付，跳过处理: orderNo={}", orderNo);
            return Result.success();
        }
        
        // 6. 处理支付结果
        if ("SUCCESS".equals(status)) {
            order.setStatus(1); // 已支付
            order.setWxPayTradeNo(wxPayTradeNo);
            order.setPayTime(LocalDateTime.now());
            paymentOrderRepository.updateById(order);
            
            // 清除幂等性缓存
            String idempotencyKey = ORDER_IDEMPOTENCY_PREFIX + order.getUserId() + ":" + order.getActivityId() + ":" + order.getType();
            redisTemplate.delete(idempotencyKey);
            
            log.info("支付成功: orderNo={}, wxPayTradeNo={}", orderNo, wxPayTradeNo);
        } else {
            order.setStatus(2); // 支付失败
            paymentOrderRepository.updateById(order);
            
            log.warn("支付失败: orderNo={}, status={}", orderNo, status);
        }
        
        return Result.success();
    }
    
    /**
     * 退款（带权限校验）
     */
    @Transactional
    public Result<Void> refund(String orderNo, Long userId, String reason) {
        PaymentOrder order = paymentOrderRepository.findByOrderNo(orderNo);
        if (order == null) {
            return Result.error("订单不存在");
        }
        
        // 权限校验：只能退款自己的订单
        if (!order.getUserId().equals(userId)) {
            log.warn("退款权限不足: orderNo={}, requestUserId={}, orderUserId={}", 
                    orderNo, userId, order.getUserId());
            return Result.error("无权操作此订单");
        }
        
        if (order.getStatus() != 1) {
            return Result.error("订单未支付，无法退款");
        }
        
        // 检查是否已退款
        if (order.getStatus() == 3) {
            return Result.error("订单已退款");
        }
        
        // TODO: 调用微信退款接口
        
        order.setStatus(3); // 已退款
        order.setRefundReason(reason);
        order.setRefundTime(LocalDateTime.now());
        paymentOrderRepository.updateById(order);
        
        log.info("退款成功: orderNo={}, userId={}, amount={}, reason={}", 
                orderNo, userId, order.getAmount(), reason);
        
        return Result.success();
    }
    
    /**
     * 查询订单状态
     */
    public Result<Map<String, Object>> getOrderStatus(String orderNo, Long userId) {
        PaymentOrder order = paymentOrderRepository.findByOrderNo(orderNo);
        if (order == null) {
            return Result.error("订单不存在");
        }
        
        // 权限校验：只能查询自己的订单
        if (!order.getUserId().equals(userId)) {
            return Result.error("无权查看此订单");
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
     * 验证回调签名
     */
    private boolean verifyCallbackSignature(Map<String, String> params) {
        try {
            String signature = params.remove("signature");
            
            // 按key排序并拼接
            StringBuilder sb = new StringBuilder();
            params.entrySet().stream()
                    .sorted(Map.Entry.comparingByKey())
                    .forEach(entry -> {
                        if (entry.getValue() != null && !entry.getValue().isEmpty()) {
                            sb.append(entry.getKey()).append("=").append(entry.getValue()).append("&");
                        }
                    });
            sb.append("key=").append(wxApiKey);
            
            // MD5签名
            String calculatedSign = md5(sb.toString()).toUpperCase();
            
            return calculatedSign.equals(signature);
        } catch (Exception e) {
            log.error("签名验证异常", e);
            return false;
        }
    }
    
    /**
     * MD5加密
     */
    private String md5(String data) throws Exception {
        MessageDigest md = MessageDigest.getInstance("MD5");
        byte[] digest = md.digest(data.getBytes(StandardCharsets.UTF_8));
        
        Formatter formatter = new Formatter();
        for (byte b : digest) {
            formatter.format("%02x", b);
        }
        return formatter.toString();
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
