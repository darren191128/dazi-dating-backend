package com.dazi.user.repository;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.dazi.user.entity.UserMatch;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;
import org.apache.ibatis.annotations.Update;

import java.util.List;

@Mapper
public interface UserMatchRepository extends BaseMapper<UserMatch> {
    
    @Select("SELECT * FROM user_match WHERE user_id = #{userId} AND target_user_id = #{targetUserId} LIMIT 1")
    UserMatch findByUserIdAndTargetUserId(@Param("userId") Long userId, @Param("targetUserId") Long targetUserId);
    
    @Select("SELECT * FROM user_match WHERE target_user_id = #{targetUserId} AND user_id = #{userId} AND action = 1 LIMIT 1")
    UserMatch findReverseLike(@Param("userId") Long userId, @Param("targetUserId") Long targetUserId);
    
    @Select("SELECT * FROM user_match WHERE user_id = #{userId} AND action = #{action} ORDER BY created_at DESC LIMIT #{offset}, #{limit}")
    List<UserMatch> findByUserIdAndAction(@Param("userId") Long userId, @Param("action") Integer action, @Param("offset") Integer offset, @Param("limit") Integer limit);
    
    @Select("SELECT * FROM user_match WHERE user_id = #{userId} AND is_mutual = 1 ORDER BY created_at DESC LIMIT #{offset}, #{limit}")
    List<UserMatch> findMutualMatches(@Param("userId") Long userId, @Param("offset") Integer offset, @Param("limit") Integer limit);
    
    @Update("UPDATE user_match SET is_mutual = 1 WHERE user_id = #{userId} AND target_user_id = #{targetUserId}")
    void updateMutual(@Param("userId") Long userId, @Param("targetUserId") Long targetUserId);
}
