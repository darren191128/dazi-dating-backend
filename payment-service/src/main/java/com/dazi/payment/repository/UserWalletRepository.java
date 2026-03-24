package com.dazi.payment.repository;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.dazi.payment.entity.UserWallet;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;
import org.apache.ibatis.annotations.Update;

@Mapper
public interface UserWalletRepository extends BaseMapper<UserWallet> {
    
    @Select("SELECT * FROM user_wallet WHERE user_id = #{userId}")
    UserWallet findByUserId(Long userId);
    
    @Update("UPDATE user_wallet SET gold_coin = gold_coin + #{amount}, " +
            "total_recharge = total_recharge + #{rechargeAmount} " +
            "WHERE user_id = #{userId}")
    int addGoldCoin(@Param("userId") Long userId, 
                    @Param("amount") Integer amount, 
                    @Param("rechargeAmount") java.math.BigDecimal rechargeAmount);
    
    @Update("UPDATE user_wallet SET gold_coin = gold_coin - #{amount}, " +
            "total_consume = total_consume + #{consumeAmount} " +
            "WHERE user_id = #{userId} AND gold_coin >= #{amount}")
    int deductGoldCoin(@Param("userId") Long userId, 
                       @Param("amount") Integer amount,
                       @Param("consumeAmount") java.math.BigDecimal consumeAmount);
    
    @Update("UPDATE user_wallet SET point = point + #{amount} WHERE user_id = #{userId}")
    int addPoint(@Param("userId") Long userId, @Param("amount") Integer amount);
    
    @Update("UPDATE user_wallet SET point = point - #{amount} WHERE user_id = #{userId} AND point >= #{amount}")
    int deductPoint(@Param("userId") Long userId, @Param("amount") Integer amount);
}
