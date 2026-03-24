package com.dazi.message.repository;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.dazi.message.entity.ChatGroupMessage;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;

import java.util.List;

@Mapper
public interface ChatGroupMessageRepository extends BaseMapper<ChatGroupMessage> {
    
    @Select("SELECT * FROM chat_group_message WHERE group_id = #{groupId} ORDER BY created_at DESC LIMIT #{offset}, #{limit}")
    List<ChatGroupMessage> findByGroupIdWithPagination(@Param("groupId") Long groupId, @Param("offset") Integer offset, @Param("limit") Integer limit);
    
    @Select("SELECT * FROM chat_group_message WHERE group_id = #{groupId} ORDER BY created_at DESC LIMIT 1")
    ChatGroupMessage findLatestByGroupId(@Param("groupId") Long groupId);
    
    @Select("SELECT COUNT(*) FROM chat_group_message WHERE group_id = #{groupId}")
    Long countByGroupId(@Param("groupId") Long groupId);
}
