package com.dazi.admin.repository;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.dazi.admin.entity.AdminUser;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;
import org.springframework.stereotype.Repository;

@Repository
public interface AdminUserRepository extends BaseMapper<AdminUser> {
    
    @Select("SELECT * FROM admin_user WHERE username = #{username} AND deleted = 0")
    AdminUser findByUsername(@Param("username") String username);
}
