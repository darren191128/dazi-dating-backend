package com.dazi.message.repository;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.dazi.message.entity.Conversation;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Select;
import java.util.List;

@Mapper
public interface ConversationRepository extends BaseMapper<Conversation> {
    
    @Select("SELECT * FROM conversation WHERE user_id = #{userId} ORDER BY last_message_time DESC")
    List<Conversation> findByUserId(Long userId);
}
