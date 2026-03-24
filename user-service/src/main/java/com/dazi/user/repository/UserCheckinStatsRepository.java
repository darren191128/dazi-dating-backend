package com.dazi.user.repository;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.dazi.user.entity.UserCheckinStats;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;
import org.apache.ibatis.annotations.Update;

import java.time.LocalDate;

@Mapper
public interface UserCheckinStatsRepository extends BaseMapper<UserCheckinStats> {
    
    @Select("SELECT * FROM user_checkin_stats WHERE user_id = #{userId}")
    UserCheckinStats findByUserId(Long userId);
    
    @Update("UPDATE user_checkin_stats SET current_consecutive_days = #{consecutiveDays}, " +
            "last_checkin_date = #{lastDate}, total_checkin_days = total_checkin_days + 1, " +
            "max_consecutive_days = GREATEST(max_consecutive_days, #{consecutiveDays}) " +
            "WHERE user_id = #{userId}")
    int updateCheckinStats(@Param("userId") Long userId, 
                           @Param("consecutiveDays") Integer consecutiveDays,
                           @Param("lastDate") LocalDate lastDate);
}
