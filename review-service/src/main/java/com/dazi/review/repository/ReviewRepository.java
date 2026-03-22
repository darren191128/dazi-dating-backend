package com.dazi.review.repository;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.dazi.review.entity.Review;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Select;
import java.util.List;

@Mapper
public interface ReviewRepository extends BaseMapper<Review> {
    
    @Select("SELECT * FROM review WHERE reviewee_id = #{userId} ORDER BY create_time DESC")
    List<Review> findByRevieweeId(Long userId);
    
    @Select("SELECT AVG(rating) FROM review WHERE reviewee_id = #{userId}")
    Double getAverageRating(Long userId);
}
