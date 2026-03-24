package com.dazi.activity.repository;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.dazi.activity.entity.Activity;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;
import org.apache.ibatis.annotations.Update;
import org.springframework.stereotype.Repository;

@Repository
public interface ActivityRepository extends BaseMapper<Activity> {
    
    @Select("SELECT * FROM activity WHERE type_name = #{typeName} AND status = 1 AND deleted = 0 ORDER BY create_time DESC")
    Page<Activity> findByTypeName(Page<Activity> page, @Param("typeName") String typeName);
    
    /**
     * 乐观锁增加参与人数
     */
    @Update("UPDATE activity SET current_participants = current_participants + 1, version = version + 1 " +
            "WHERE id = #{activityId} AND version = #{version} AND current_participants < max_participants")
    int increaseParticipantCount(@Param("activityId") Long activityId, @Param("version") Integer version);
    
    /**
     * 减少参与人数
     */
    @Update("UPDATE activity SET current_participants = current_participants - 1 " +
            "WHERE id = #{activityId} AND current_participants > 0")
    int decreaseParticipantCount(@Param("activityId") Long activityId);
}
