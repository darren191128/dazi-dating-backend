package com.dazi.activity.repository;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.dazi.activity.entity.ActivityParticipant;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ActivityParticipantRepository extends BaseMapper<ActivityParticipant> {
    
    @Select("SELECT * FROM activity_participant WHERE activity_id = #{activityId} AND status = 1")
    List<ActivityParticipant> findByActivityId(@Param("activityId") Long activityId);
    
    @Select("SELECT * FROM activity_participant WHERE activity_id = #{activityId} AND user_id = #{userId}")
    ActivityParticipant findByActivityIdAndUserId(@Param("activityId") Long activityId, @Param("userId") Long userId);
}
