package com.dazi.message.repository;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.dazi.message.entity.ChatGroupMember;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;

import java.util.List;

@Mapper
public interface ChatGroupMemberRepository extends BaseMapper<ChatGroupMember> {
    
    @Select("SELECT * FROM chat_group_member WHERE group_id = #{groupId} ORDER BY role DESC, join_time ASC")
    List<ChatGroupMember> findByGroupId(@Param("groupId") Long groupId);
    
    @Select("SELECT * FROM chat_group_member WHERE group_id = #{groupId} AND user_id = #{userId}")
    ChatGroupMember findByGroupIdAndUserId(@Param("groupId") Long groupId, @Param("userId") Long userId);
    
    @Select("SELECT COUNT(*) FROM chat_group_member WHERE group_id = #{groupId}")
    Long countByGroupId(@Param("groupId") Long groupId);
    
    @Select("SELECT group_id FROM chat_group_member WHERE user_id = #{userId}")
    List<Long> findGroupIdsByUserId(@Param("userId") Long userId);
}
