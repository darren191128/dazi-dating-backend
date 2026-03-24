package com.dazi.user.service;

import com.dazi.common.result.Result;
import com.dazi.user.dto.CheckinRecordDTO;
import com.dazi.user.dto.CheckinResultDTO;
import com.dazi.user.dto.CheckinStatusDTO;

import java.util.List;

/**
 * 签到服务接口
 */
public interface CheckinService {
    
    /**
     * 每日签到
     */
    Result<CheckinResultDTO> checkin(Long userId);
    
    /**
     * 查询签到状态
     */
    Result<CheckinStatusDTO> getCheckinStatus(Long userId);
    
    /**
     * 获取签到记录
     */
    Result<List<CheckinRecordDTO>> getCheckinRecords(Long userId, String month);
    
    /**
     * 初始化用户签到统计
     */
    void initUserCheckinStats(Long userId);
}
