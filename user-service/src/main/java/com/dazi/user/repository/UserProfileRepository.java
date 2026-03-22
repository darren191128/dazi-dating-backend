package com.dazi.user.repository;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.dazi.user.entity.UserProfile;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Select;

@Mapper
public interface UserProfileRepository extends BaseMapper<UserProfile> {
    
    @Select("SELECT * FROM user_profile WHERE user_id = #{userId}")
    UserProfile findByUserId(Long userId);
}
