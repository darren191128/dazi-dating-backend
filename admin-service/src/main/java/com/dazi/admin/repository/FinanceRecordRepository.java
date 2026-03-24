package com.dazi.admin.repository;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.dazi.admin.entity.FinanceRecord;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;
import org.springframework.stereotype.Repository;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;

@Repository
public interface FinanceRecordRepository extends BaseMapper<FinanceRecord> {
    
    @Select("SELECT * FROM finance_record WHERE user_id = #{userId} ORDER BY create_time DESC")
    List<FinanceRecord> findByUserId(@Param("userId") Long userId);
    
    @Select("SELECT * FROM finance_record WHERE order_no = #{orderNo}")
    FinanceRecord findByOrderNo(@Param("orderNo") String orderNo);
    
    @Select("SELECT SUM(amount) FROM finance_record WHERE type = #{type} AND create_time BETWEEN #{start} AND #{end}")
    BigDecimal sumAmountByTypeAndTime(@Param("type") Integer type, 
                                       @Param("start") LocalDateTime start, 
                                       @Param("end") LocalDateTime end);
}
