package com.dazi.user.repository;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.dazi.user.entity.UserVisitor;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;
import org.apache.ibatis.annotations.Update;

import java.util.List;

@Mapper
public interface UserVisitorRepository extends BaseMapper<UserVisitor> {
    
    @Select("SELECT * FROM user_visitor WHERE user_id = #{userId} AND visitor_id = #{visitorId} LIMIT 1")
    UserVisitor findByUserIdAndVisitorId(@Param("userId") Long userId, @Param("visitorId") Long visitorId);
    
    @Select("SELECT * FROM user_visitor WHERE user_id = #{userId} ORDER BY last_visit_at DESC LIMIT #{offset}, #{limit}")
    List<UserVisitor> findByUserIdWithPagination(@Param("userId") Long userId, @Param("offset") Integer offset, @Param("limit") Integer limit);
    
    @Update("UPDATE user_visitor SET visit_count = visit_count + 1, last_visit_at = NOW(), updated_at = NOW() WHERE id = #{id}")
    void incrementVisitCount(@Param("id") Long id);
}
