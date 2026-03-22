package com.dazi.message.repository;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.dazi.message.entity.Message;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Select;
import java.util.List;

@Mapper
public interface MessageRepository extends BaseMapper<Message> {
    
    @Select("SELECT * FROM message WHERE (sender_id = #{userId} AND receiver_id = #{targetId}) OR (sender_id = #{targetId} AND receiver_id = #{userId}) ORDER BY create_time ASC")
    List<Message> findConversation(Long userId, Long targetId);
    
    @Select("SELECT COUNT(*) FROM message WHERE receiver_id = #{userId} AND status = 0")
    Integer countUnread(Long userId);
}
