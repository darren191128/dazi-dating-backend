package com.dazi.user.repository;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.dazi.user.entity.UserFollow;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;

import java.util.List;

@Mapper
public interface UserFollowRepository extends BaseMapper<UserFollow> {
    
    @Select("SELECT * FROM user_follow WHERE user_id = #{userId} AND follow_user_id = #{followUserId} LIMIT 1")
    UserFollow findByUserIdAndFollowUserId(@Param("userId") Long userId, @Param("followUserId") Long followUserId);
    
    @Select("SELECT * FROM user_follow WHERE user_id = #{userId} ORDER BY created_at DESC LIMIT #{offset}, #{limit}")
    List<UserFollow> findFollowingByUserId(@Param("userId") Long userId, @Param("offset") Integer offset, @Param("limit") Integer limit);
    
    @Select("SELECT * FROM user_follow WHERE follow_user_id = #{followUserId} ORDER BY created_at DESC LIMIT #{offset}, #{limit}")
    List<UserFollow> findFollowersByUserId(@Param("followUserId") Long followUserId, @Param("offset") Integer offset, @Param("limit") Integer limit);
    
    @Select("SELECT COUNT(*) FROM user_follow WHERE user_id = #{userId}")
    Integer countFollowing(@Param("userId") Long userId);
    
    @Select("SELECT COUNT(*) FROM user_follow WHERE follow_user_id = #{userId}")
    Integer countFollowers(@Param("userId") Long userId);
    
    @Select("SELECT COUNT(*) FROM user_follow WHERE user_id = #{userId} AND follow_user_id = #{followUserId}")
    Integer existsByUserIdAndFollowUserId(@Param("userId") Long userId, @Param("followUserId") Long followUserId);
}
