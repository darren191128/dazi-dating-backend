package com.dazi.payment.repository;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.dazi.payment.entity.PaymentOrder;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Select;

@Mapper
public interface PaymentOrderRepository extends BaseMapper<PaymentOrder> {
    
    @Select("SELECT * FROM payment_order WHERE order_no = #{orderNo}")
    PaymentOrder findByOrderNo(String orderNo);
    
    @Select("SELECT * FROM payment_order WHERE wx_pay_trade_no = #{tradeNo}")
    PaymentOrder findByWxPayTradeNo(String tradeNo);
}
