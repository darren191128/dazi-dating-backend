package com.dazi.admin.repository;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.dazi.admin.entity.SystemConfig;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface SystemConfigRepository extends BaseMapper<SystemConfig> {
    
    @Select("SELECT * FROM system_config WHERE config_key = #{key}")
    SystemConfig findByKey(@Param("key") String key);
    
    @Select("SELECT * FROM system_config WHERE category = #{category}")
    List<SystemConfig> findByCategory(@Param("category") String category);
}
