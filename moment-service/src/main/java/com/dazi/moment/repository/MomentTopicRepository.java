package com.dazi.moment.repository;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.dazi.moment.entity.MomentTopic;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;

import java.util.List;

@Mapper
public interface MomentTopicRepository extends BaseMapper<MomentTopic> {
    
    @Select("SELECT * FROM moment_topic WHERE moment_id = #{momentId}")
    List<MomentTopic> findByMomentId(@Param("momentId") Long momentId);
    
    @Select("SELECT * FROM moment_topic WHERE topic_id = #{topicId} ORDER BY created_at DESC LIMIT #{offset}, #{limit}")
    List<MomentTopic> findByTopicId(@Param("topicId") Long topicId, @Param("offset") Integer offset, @Param("limit") Integer limit);
    
    @Select("SELECT COUNT(*) FROM moment_topic WHERE topic_id = #{topicId}")
    Integer countByTopicId(@Param("topicId") Long topicId);
}
