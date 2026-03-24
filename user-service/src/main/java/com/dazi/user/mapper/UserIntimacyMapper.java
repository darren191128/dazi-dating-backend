package com.dazi.user.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.dazi.user.entity.UserIntimacy;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;

import java.util.List;

@Mapper
public interface UserIntimacyMapper extends BaseMapper<UserIntimacy> {
    
    @Select("SELECT * FROM user_intimacy WHERE user_id = #{userId} AND friend_id = #{friendId}")
    UserIntimacy selectByUserAndFriend(@Param("userId") Long userId, @Param("friendId") Long friendId);
    
    @Select("SELECT * FROM user_intimacy WHERE user_id = #{userId} ORDER BY intimacy_score DESC LIMIT #{limit}")
    List<UserIntimacy> selectRankingByUserId(@Param("userId") Long userId, @Param("limit") Integer limit);
}
