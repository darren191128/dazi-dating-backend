package com.dazi.admin.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.dazi.admin.entity.UserReport;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;

import java.util.List;

@Mapper
public interface UserReportMapper extends BaseMapper<UserReport> {
    
    @Select("SELECT * FROM user_report WHERE status = #{status} ORDER BY created_at DESC")
    List<UserReport> selectByStatus(@Param("status") Integer status);
}
