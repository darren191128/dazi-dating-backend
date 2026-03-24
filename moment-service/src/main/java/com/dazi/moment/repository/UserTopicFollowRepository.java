package com.dazi.moment.repository;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.dazi.moment.entity.UserTopicFollow;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;

@Mapper
public interface UserTopicFollowRepository extends BaseMapper<UserTopicFollow> {
    
    @Select("SELECT * FROM user_topic_follow WHERE user_id = #{userId} AND topic_id = #{topicId}")
    UserTopicFollow findByUserIdAndTopicId(@Param("userId") Long userId, @Param("topicId") Long topicId);
    
    @Select("SELECT COUNT(*) FROM user_topic_follow WHERE user_id = #{userId}")
    Integer countByUserId(@Param("userId") Long userId);
    
    @Select("SELECT COUNT(*) FROM user_topic_follow WHERE topic_id = #{topicId}")
    Integer countByTopicId(@Param("topicId") Long topicId);
}
