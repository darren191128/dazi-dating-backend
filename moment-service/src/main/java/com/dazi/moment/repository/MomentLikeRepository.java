package com.dazi.moment.repository;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.dazi.moment.entity.MomentLike;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;

@Mapper
public interface MomentLikeRepository extends BaseMapper<MomentLike> {
    
    @Select("SELECT * FROM moment_like WHERE moment_id = #{momentId} AND user_id = #{userId} LIMIT 1")
    MomentLike findByMomentIdAndUserId(@Param("momentId") Long momentId, @Param("userId") Long userId);
    
    @Select("SELECT COUNT(*) FROM moment_like WHERE moment_id = #{momentId}")
    Integer countByMomentId(@Param("momentId") Long momentId);
    
    @Select("SELECT COUNT(*) FROM moment_like WHERE moment_id = #{momentId} AND user_id = #{userId}")
    Integer existsByMomentIdAndUserId(@Param("momentId") Long momentId, @Param("userId") Long userId);
}
