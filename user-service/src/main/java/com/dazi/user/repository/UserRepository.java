package com.dazi.user.repository;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.dazi.user.entity.User;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Select;

@Mapper
public interface UserRepository extends BaseMapper<User> {
    
    @Select("SELECT * FROM user WHERE open_id = #{openId} AND deleted = 0")
    User findByOpenId(String openId);
    
    @Select("SELECT * FROM user WHERE phone = #{phone} AND deleted = 0")
    User findByPhone(String phone);
}
