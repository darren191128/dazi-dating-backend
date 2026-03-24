package com.dazi.user.repository;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.dazi.user.entity.UserCheckin;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;

import java.time.LocalDate;
import java.util.List;

@Mapper
public interface UserCheckinRepository extends BaseMapper<UserCheckin> {
    
    @Select("SELECT * FROM user_checkin WHERE user_id = #{userId} AND checkin_date = #{date}")
    UserCheckin findByUserIdAndDate(@Param("userId") Long userId, @Param("date") LocalDate date);
    
    @Select("SELECT * FROM user_checkin WHERE user_id = #{userId} " +
            "AND checkin_date >= #{startDate} AND checkin_date <= #{endDate} " +
            "ORDER BY checkin_date DESC")
    List<UserCheckin> findByUserIdAndMonth(@Param("userId") Long userId, 
                                            @Param("startDate") LocalDate startDate, 
                                            @Param("endDate") LocalDate endDate);
    
    @Select("SELECT * FROM user_checkin WHERE user_id = #{userId} ORDER BY checkin_date DESC LIMIT 1")
    UserCheckin findLastCheckin(Long userId);
    
    @Select("SELECT COUNT(*) FROM user_checkin WHERE user_id = #{userId}")
    int countByUserId(Long userId);
    
    @Select("SELECT COUNT(*) FROM user_checkin WHERE user_id = #{userId} " +
            "AND checkin_date >= #{startDate} AND checkin_date <= #{endDate}")
    int countByUserIdAndMonth(@Param("userId") Long userId, 
                               @Param("startDate") LocalDate startDate, 
                               @Param("endDate") LocalDate endDate);
}
