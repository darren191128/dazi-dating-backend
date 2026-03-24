package com.dazi.rtc.repository;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.dazi.rtc.entity.CallRecord;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;
import org.apache.ibatis.annotations.Update;

import java.util.List;

@Mapper
public interface CallRecordRepository extends BaseMapper<CallRecord> {
    
    @Select("SELECT * FROM call_record WHERE caller_id = #{userId} OR callee_id = #{userId} ORDER BY created_at DESC")
    List<CallRecord> findByCallerIdOrCalleeId(@Param("userId") Long userId);
    
    @Select("SELECT * FROM call_record WHERE caller_id = #{userId} OR callee_id = #{userId} ORDER BY created_at DESC LIMIT #{offset}, #{limit}")
    List<CallRecord> findByCallerIdOrCalleeIdWithPagination(@Param("userId") Long userId, @Param("offset") Integer offset, @Param("limit") Integer limit);
    
    @Select("SELECT COUNT(*) FROM call_record WHERE caller_id = #{userId} OR callee_id = #{userId}")
    Long countByCallerIdOrCalleeId(@Param("userId") Long userId);
    
    @Update("UPDATE call_record SET status = #{status} WHERE id = #{callId}")
    int updateStatus(@Param("callId") Long callId, @Param("status") Integer status);
    
    @Update("UPDATE call_record SET status = #{status}, start_time = NOW() WHERE id = #{callId}")
    int updateStatusAndStartTime(@Param("callId") Long callId, @Param("status") Integer status);
    
    @Update("UPDATE call_record SET status = #{status}, end_time = NOW(), duration = #{duration} WHERE id = #{callId}")
    int updateStatusAndEndTime(@Param("callId") Long callId, @Param("status") Integer status, @Param("duration") Integer duration);
}
