package com.dazi.admin.repository;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.dazi.admin.entity.AdminPermission;
import com.dazi.admin.entity.AdminRolePermission;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface AdminPermissionRepository extends BaseMapper<AdminPermission> {
    
    @Select("SELECT p.* FROM admin_permission p " +
            "INNER JOIN admin_role_permission rp ON p.id = rp.permission_id " +
            "WHERE rp.role = #{role}")
    List<AdminPermission> findByRole(@Param("role") Integer role);
}
