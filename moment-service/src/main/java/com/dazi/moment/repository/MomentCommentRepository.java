package com.dazi.moment.repository;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.dazi.moment.entity.MomentComment;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;

import java.util.List;

@Mapper
public interface MomentCommentRepository extends BaseMapper<MomentComment> {
    
    @Select("SELECT * FROM moment_comment WHERE moment_id = #{momentId} AND parent_id IS NULL AND status = 1 AND deleted = 0 ORDER BY created_at DESC LIMIT #{offset}, #{limit}")
    List<MomentComment> findRootCommentsByMomentId(@Param("momentId") Long momentId, @Param("offset") Integer offset, @Param("limit") Integer limit);
    
    @Select("SELECT * FROM moment_comment WHERE parent_id = #{parentId} AND status = 1 AND deleted = 0 ORDER BY created_at ASC")
    List<MomentComment> findRepliesByParentId(@Param("parentId") Long parentId);
    
    @Select("SELECT COUNT(*) FROM moment_comment WHERE moment_id = #{momentId} AND status = 1 AND deleted = 0")
    Integer countByMomentId(@Param("momentId") Long momentId);
}
