package com.dazi.activity.repository;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.dazi.activity.entity.Activity;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Select;
import java.util.List;

@Mapper
public interface ActivityRepository extends BaseMapper<Activity> {
    
    @Select("SELECT * FROM activity WHERE status = 1 AND deleted = 0 AND end_time > NOW() ORDER BY create_time DESC")
    List<Activity> findActiveActivities();
    
    @Select("SELECT * FROM activity WHERE user_id = #{userId} AND deleted = 0 ORDER BY create_time DESC")
    List<Activity> findByUserId(Long userId);
    
    @Select("SELECT * FROM activity WHERE type = #{type} AND status = 1 AND deleted = 0 AND end_time > NOW()")
    List<Activity> findByType(Integer type);
}
