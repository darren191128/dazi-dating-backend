package com.dazi.rtc.service;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.dazi.common.result.Result;
import com.dazi.rtc.dto.CallRecordDTO;
import com.dazi.rtc.dto.CallTokenDTO;

public interface RtcService {
    
    /**
     * 获取TRTC用户签名
     */
    Result<CallTokenDTO> getToken(Long userId, String roomId);
    
    /**
     * 发起通话
     */
    Result<CallRecordDTO> startCall(Long callerId, Long calleeId, Integer callType);
    
    /**
     * 接受通话
     */
    Result<Void> acceptCall(Long userId, Long callId);
    
    /**
     * 拒绝通话
     */
    Result<Void> rejectCall(Long userId, Long callId);
    
    /**
     * 结束通话
     */
    Result<Void> endCall(Long userId, Long callId);
    
    /**
     * 获取通话记录
     */
    Result<Page<CallRecordDTO>> getCallRecords(Long userId, Integer page, Integer pageSize);
}
