package com.dazi.moment.repository;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.dazi.moment.entity.Moment;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;
import org.apache.ibatis.annotations.Update;

import java.util.List;

@Mapper
public interface MomentRepository extends BaseMapper<Moment> {
    
    @Select("SELECT * FROM moment WHERE status = 1 AND deleted = 0 ORDER BY is_top DESC, created_at DESC LIMIT #{offset}, #{limit}")
    List<Moment> findAllWithPagination(@Param("offset") Integer offset, @Param("limit") Integer limit);
    
    @Select("SELECT * FROM moment WHERE status = 1 AND deleted = 0 ORDER BY created_at DESC")
    List<Moment> findAllActiveMoments();
    
    @Select("SELECT * FROM moment WHERE user_id = #{userId} AND status = 1 AND deleted = 0 ORDER BY is_top DESC, created_at DESC LIMIT #{offset}, #{limit}")
    List<Moment> findByUserIdWithPagination(@Param("userId") Long userId, @Param("offset") Integer offset, @Param("limit") Integer limit);
    
    @Select("SELECT * FROM moment WHERE user_id IN (SELECT follow_user_id FROM user_follow WHERE user_id = #{userId}) AND status = 1 AND deleted = 0 ORDER BY created_at DESC LIMIT #{offset}, #{limit}")
    List<Moment> findFollowingMoments(@Param("userId") Long userId, @Param("offset") Integer offset, @Param("limit") Integer limit);
    
    @Update("UPDATE moment SET like_count = like_count + 1 WHERE id = #{momentId}")
    void incrementLikeCount(@Param("momentId") Long momentId);
    
    @Update("UPDATE moment SET like_count = like_count - 1 WHERE id = #{momentId} AND like_count > 0")
    void decrementLikeCount(@Param("momentId") Long momentId);
    
    @Update("UPDATE moment SET comment_count = comment_count + 1 WHERE id = #{momentId}")
    void incrementCommentCount(@Param("momentId") Long momentId);
    
    @Update("UPDATE moment SET view_count = view_count + 1 WHERE id = #{momentId}")
    void incrementViewCount(@Param("momentId") Long momentId);
}
