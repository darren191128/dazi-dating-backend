package com.dazi.payment.repository;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.dazi.payment.entity.TransactionRecord;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;

import java.util.List;

@Mapper
public interface TransactionRecordRepository extends BaseMapper<TransactionRecord> {
    
    @Select("SELECT * FROM transaction_record WHERE user_id = #{userId} " +
            "ORDER BY created_at DESC LIMIT #{limit} OFFSET #{offset}")
    List<TransactionRecord> findByUserIdWithPagination(@Param("userId") Long userId, 
                                                        @Param("offset") int offset, 
                                                        @Param("limit") int limit);
    
    @Select("SELECT * FROM transaction_record WHERE user_id = #{userId} AND currency_type = #{currencyType} " +
            "ORDER BY created_at DESC LIMIT #{limit} OFFSET #{offset}")
    List<TransactionRecord> findByUserIdAndCurrencyType(@Param("userId") Long userId, 
                                                         @Param("currencyType") String currencyType,
                                                         @Param("offset") int offset, 
                                                         @Param("limit") int limit);
    
    @Select("SELECT * FROM transaction_record WHERE user_id = #{userId} AND type = #{type} " +
            "ORDER BY created_at DESC LIMIT #{limit} OFFSET #{offset}")
    List<TransactionRecord> findByUserIdAndType(@Param("userId") Long userId, 
                                                 @Param("type") Integer type,
                                                 @Param("offset") int offset, 
                                                 @Param("limit") int limit);
    
    @Select("SELECT COUNT(*) FROM transaction_record WHERE user_id = #{userId}")
    int countByUserId(Long userId);
    
    @Select("SELECT COUNT(*) FROM transaction_record WHERE user_id = #{userId} AND currency_type = #{currencyType}")
    int countByUserIdAndCurrencyType(@Param("userId") Long userId, @Param("currencyType") String currencyType);
    
    @Select("SELECT COUNT(*) FROM transaction_record WHERE user_id = #{userId} AND type = #{type}")
    int countByUserIdAndType(@Param("userId") Long userId, @Param("type") Integer type);
}
