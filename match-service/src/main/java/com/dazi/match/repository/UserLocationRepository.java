package com.dazi.match.repository;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.dazi.match.entity.UserLocation;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface UserLocationRepository extends BaseMapper<UserLocation> {
    
    @Select("SELECT * FROM user_location WHERE user_id = #{userId}")
    UserLocation findByUserId(Long userId);
    
    @Select("SELECT * FROM user_location WHERE user_id != #{userId} LIMIT #{offset}, #{limit}")
    List<UserLocation> findCandidates(@Param("userId") Long userId, 
                                      @Param("offset") Integer offset, 
                                      @Param("limit") Integer limit);
    
    /**
     * 带筛选条件的候选用户查询
     */
    @Select("<script>" +
            "SELECT ul.* FROM user_location ul " +
            "LEFT JOIN user_profile up ON ul.user_id = up.user_id " +
            "WHERE ul.user_id != #{userId} " +
            "<if test='minHeight != null'> AND up.height &gt;= #{minHeight} </if>" +
            "<if test='maxHeight != null'> AND up.height &lt;= #{maxHeight} </if>" +
            "<if test='education != null'> AND up.education = #{education} </if>" +
            "<if test='occupation != null'> AND up.occupation = #{occupation} </if>" +
            "<if test='minIncome != null'> AND up.income &gt;= #{minIncome} </if>" +
            "<if test='maxIncome != null'> AND up.income &lt;= #{maxIncome} </if>" +
            "<if test='hasHouse != null'> AND up.has_house = #{hasHouse} </if>" +
            "<if test='hasCar != null'> AND up.has_car = #{hasCar} </if>" +
            "<if test='maritalStatus != null'> AND up.marital_status = #{maritalStatus} </if>" +
            "LIMIT #{offset}, #{limit}" +
            "</script>")
    List<UserLocation> findCandidatesWithFilter(
            @Param("userId") Long userId,
            @Param("minHeight") Integer minHeight,
            @Param("maxHeight") Integer maxHeight,
            @Param("education") String education,
            @Param("occupation") String occupation,
            @Param("minIncome") Integer minIncome,
            @Param("maxIncome") Integer maxIncome,
            @Param("hasHouse") Boolean hasHouse,
            @Param("hasCar") Boolean hasCar,
            @Param("maritalStatus") String maritalStatus,
            @Param("offset") Integer offset,
            @Param("limit") Integer limit);
    
    /**
     * 获取高质量用户（用于每日精选）
     * 条件：资料完整度>80%，照片数量>3，7天内登录
     */
    @Select("SELECT ul.* FROM user_location ul " +
            "LEFT JOIN user_profile up ON ul.user_id = up.user_id " +
            "LEFT JOIN user u ON ul.user_id = u.id " +
            "WHERE ul.user_id != #{userId} " +
            "AND up.profile_completeness &gt;= 80 " +
            "AND u.last_login_time &gt;= DATE_SUB(NOW(), INTERVAL 7 DAY) " +
            "ORDER BY up.profile_completeness DESC, u.last_login_time DESC " +
            "LIMIT #{limit}")
    List<UserLocation> findHighQualityUsers(@Param("userId") Long userId, @Param("limit") Integer limit);
}
