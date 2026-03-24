package com.dazi.moment.repository;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.dazi.moment.entity.Topic;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;
import org.apache.ibatis.annotations.Update;

import java.util.List;

@Mapper
public interface TopicRepository extends BaseMapper<Topic> {
    
    @Select("SELECT * FROM topic WHERE status = 1 ORDER BY sort_order ASC, id ASC")
    List<Topic> findAllActive();
    
    @Select("SELECT * FROM topic WHERE status = 1 AND name LIKE CONCAT('%', #{keyword}, '%') ORDER BY sort_order ASC")
    List<Topic> searchByName(@Param("keyword") String keyword);
    
    @Select("SELECT t.* FROM topic t " +
            "INNER JOIN moment_topic mt ON t.id = mt.topic_id " +
            "WHERE mt.moment_id = #{momentId}")
    List<Topic> findByMomentId(@Param("momentId") Long momentId);
    
    @Update("UPDATE topic SET post_count = post_count + 1 WHERE id = #{topicId}")
    void incrementPostCount(@Param("topicId") Long topicId);
    
    @Update("UPDATE topic SET follow_count = follow_count + 1 WHERE id = #{topicId}")
    void incrementFollowCount(@Param("topicId") Long topicId);
    
    @Update("UPDATE topic SET follow_count = follow_count - 1 WHERE id = #{topicId} AND follow_count > 0")
    void decrementFollowCount(@Param("topicId") Long topicId);
}
