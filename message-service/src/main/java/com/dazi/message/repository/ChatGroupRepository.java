package com.dazi.message.repository;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.dazi.message.entity.ChatGroup;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;
import org.apache.ibatis.annotations.Update;

import java.util.List;

@Mapper
public interface ChatGroupRepository extends BaseMapper<ChatGroup> {
    
    @Select("SELECT g.* FROM chat_group g " +
            "INNER JOIN chat_group_member m ON g.id = m.group_id " +
            "WHERE m.user_id = #{userId} AND g.status = 1 " +
            "ORDER BY g.updated_at DESC")
    List<ChatGroup> findByMemberUserId(@Param("userId") Long userId);
    
    @Update("UPDATE chat_group SET member_count = member_count + 1 WHERE id = #{groupId}")
    int incrementMemberCount(@Param("groupId") Long groupId);
    
    @Update("UPDATE chat_group SET member_count = member_count - 1 WHERE id = #{groupId}")
    int decrementMemberCount(@Param("groupId") Long groupId);
}
