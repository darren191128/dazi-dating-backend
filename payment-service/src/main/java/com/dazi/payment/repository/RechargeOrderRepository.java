package com.dazi.payment.repository;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.dazi.payment.entity.RechargeOrder;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Select;
import org.apache.ibatis.annotations.Update;

import java.util.List;

@Mapper
public interface RechargeOrderRepository extends BaseMapper<RechargeOrder> {
    
    @Select("SELECT * FROM recharge_order WHERE order_no = #{orderNo}")
    RechargeOrder findByOrderNo(String orderNo);
    
    @Select("SELECT * FROM recharge_order WHERE user_id = #{userId} " +
            "ORDER BY created_at DESC LIMIT #{limit} OFFSET #{offset}")
    List<RechargeOrder> findByUserIdWithPagination(Long userId, int offset, int limit);
    
    @Select("SELECT * FROM recharge_order WHERE wx_pay_trade_no = #{tradeNo}")
    RechargeOrder findByWxPayTradeNo(String tradeNo);
    
    @Update("UPDATE recharge_order SET status = #{status}, wx_pay_trade_no = #{wxPayTradeNo}, " +
            "pay_time = NOW() WHERE order_no = #{orderNo} AND status = 0")
    int updateStatusToPaid(String orderNo, Integer status, String wxPayTradeNo);
}
