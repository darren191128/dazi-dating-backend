package com.dazi.rtc.service.impl;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.dazi.common.result.Result;
import com.dazi.rtc.dto.CallRecordDTO;
import com.dazi.rtc.dto.CallTokenDTO;
import com.dazi.rtc.entity.CallRecord;
import com.dazi.rtc.repository.CallRecordRepository;
import com.dazi.rtc.service.RtcService;
import com.dazi.rtc.util.TRTCSigGenerator;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.time.temporal.ChronoUnit;
import java.util.List;
import java.util.UUID;
import java.util.concurrent.TimeUnit;
import java.util.stream.Collectors;

@Slf4j
@Service
@RequiredArgsConstructor
public class RtcServiceImpl implements RtcService {
    
    private final CallRecordRepository callRecordRepository;
    private final RedisTemplate<String, Object> redisTemplate;
    
    @Value("${trtc.sdk-app-id:0}")
    private Integer sdkAppId;
    
    @Value("${trtc.secret-key:}")
    private String secretKey;
    
    @Value("${trtc.expire-time:86400}")
    private Long expireTime;
    
    @Override
    public Result<CallTokenDTO> getToken(Long userId, String roomId) {
        if (sdkAppId == 0 || secretKey == null || secretKey.isEmpty()) {
            return Result.error("TRTC配置未设置");
        }
        
        String userIdStr = "user_" + userId;
        String userSig = TRTCSigGenerator.genUserSig(sdkAppId, secretKey, userIdStr, expireTime);
        
        CallTokenDTO tokenDTO = new CallTokenDTO();
        tokenDTO.setUserSig(userSig);
        tokenDTO.setSdkAppId(sdkAppId);
        tokenDTO.setUserId(userIdStr);
        tokenDTO.setRoomId(roomId);
        tokenDTO.setExpireTime(expireTime);
        
        return Result.success(tokenDTO);
    }
    
    @Override
    @Transactional
    public Result<CallRecordDTO> startCall(Long callerId, Long calleeId, Integer callType) {
        // 检查是否已有进行中的通话
        String activeCallKey = "call:active:" + callerId;
        if (Boolean.TRUE.equals(redisTemplate.hasKey(activeCallKey))) {
            return Result.error("您有正在进行的通话");
        }
        
        String calleeActiveKey = "call:active:" + calleeId;
        if (Boolean.TRUE.equals(redisTemplate.hasKey(calleeActiveKey))) {
            return Result.error("对方正在通话中");
        }
        
        // 生成房间ID
        String roomId = UUID.randomUUID().toString().replace("-", "").substring(0, 16);
        
        // 创建通话记录
        CallRecord callRecord = new CallRecord();
        callRecord.setCallerId(callerId);
        callRecord.setCalleeId(calleeId);
        callRecord.setCallType(callType);
        callRecord.setStatus(0); // 待接听
        callRecord.setRoomId(roomId);
        callRecord.setCreatedAt(LocalDateTime.now());
        
        callRecordRepository.insert(callRecord);
        
        // 缓存通话状态
        redisTemplate.opsForValue().set(activeCallKey, callRecord.getId(), 5, TimeUnit.MINUTES);
        redisTemplate.opsForValue().set(calleeActiveKey, callRecord.getId(), 5, TimeUnit.MINUTES);
        
        // 发送通话通知（通过WebSocket或消息队列）
        // TODO: 集成WebSocket通知
        
        CallRecordDTO dto = convertToDTO(callRecord);
        return Result.success(dto);
    }
    
    @Override
    @Transactional
    public Result<Void> acceptCall(Long userId, Long callId) {
        CallRecord callRecord = callRecordRepository.selectById(callId);
        if (callRecord == null) {
            return Result.notFound();
        }
        
        if (!callRecord.getCalleeId().equals(userId)) {
            return Result.forbidden();
        }
        
        if (callRecord.getStatus() != 0) {
            return Result.error("通话状态已变更");
        }
        
        // 更新通话状态为通话中
        callRecordRepository.updateStatusAndStartTime(callId, 1);
        
        return Result.success();
    }
    
    @Override
    @Transactional
    public Result<Void> rejectCall(Long userId, Long callId) {
        CallRecord callRecord = callRecordRepository.selectById(callId);
        if (callRecord == null) {
            return Result.notFound();
        }
        
        if (!callRecord.getCalleeId().equals(userId)) {
            return Result.forbidden();
        }
        
        if (callRecord.getStatus() != 0) {
            return Result.error("通话状态已变更");
        }
        
        // 更新通话状态为已拒绝
        callRecord.setStatus(3);
        callRecord.setEndTime(LocalDateTime.now());
        callRecordRepository.updateById(callRecord);
        
        // 清除缓存
        clearCallCache(callRecord);
        
        return Result.success();
    }
    
    @Override
    @Transactional
    public Result<Void> endCall(Long userId, Long callId) {
        CallRecord callRecord = callRecordRepository.selectById(callId);
        if (callRecord == null) {
            return Result.notFound();
        }
        
        if (!callRecord.getCallerId().equals(userId) && !callRecord.getCalleeId().equals(userId)) {
            return Result.forbidden();
        }
        
        if (callRecord.getStatus() != 1) {
            return Result.error("通话状态已变更");
        }
        
        // 计算通话时长
        LocalDateTime startTime = callRecord.getStartTime();
        LocalDateTime endTime = LocalDateTime.now();
        int duration = (int) ChronoUnit.SECONDS.between(startTime, endTime);
        
        // 更新通话状态为已结束
        callRecord.setStatus(2);
        callRecord.setEndTime(endTime);
        callRecord.setDuration(duration);
        callRecordRepository.updateById(callRecord);
        
        // 清除缓存
        clearCallCache(callRecord);
        
        return Result.success();
    }
    
    @Override
    public Result<Page<CallRecordDTO>> getCallRecords(Long userId, Integer page, Integer pageSize) {
        Page<CallRecord> pageParam = new Page<>(page, pageSize);
        
        // 使用自定义查询
        List<CallRecord> records = callRecordRepository.findByCallerIdOrCalleeIdWithPagination(
            userId, (page - 1) * pageSize, pageSize);
        Long total = callRecordRepository.countByCallerIdOrCalleeId(userId);
        
        List<CallRecordDTO> dtoList = records.stream()
            .map(this::convertToDTO)
            .collect(Collectors.toList());
        
        Page<CallRecordDTO> resultPage = new Page<>(page, pageSize, total);
        resultPage.setRecords(dtoList);
        
        return Result.success(resultPage);
    }
    
    private void clearCallCache(CallRecord callRecord) {
        String callerKey = "call:active:" + callRecord.getCallerId();
        String calleeKey = "call:active:" + callRecord.getCalleeId();
        redisTemplate.delete(callerKey);
        redisTemplate.delete(calleeKey);
    }
    
    private CallRecordDTO convertToDTO(CallRecord callRecord) {
        CallRecordDTO dto = new CallRecordDTO();
        BeanUtils.copyProperties(callRecord, dto);
        return dto;
    }
}
