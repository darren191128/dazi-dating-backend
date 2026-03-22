package com.dazi.activity.repository;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.dazi.activity.entity.ActivityParticipant;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Select;
import java.util.List;

@Mapper
public interface ActivityParticipantRepository extends BaseMapper<ActivityParticipant> {
    
    @Select("SELECT * FROM activity_participant WHERE activity_id = #{activityId}")
    List<ActivityParticipant> findByActivityId(Long activityId);
    
    @Select("SELECT * FROM activity_participant WHERE user_id = #{userId} ORDER BY create_time DESC")
    List<ActivityParticipant> findByUserId(Long userId);
    
    @Select("SELECT COUNT(*) FROM activity_participant WHERE activity_id = #{activityId} AND status = 1")
    Integer countParticipants(Long activityId);
}
