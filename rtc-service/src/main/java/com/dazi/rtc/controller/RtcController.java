package com.dazi.rtc.controller;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.dazi.common.result.Result;
import com.dazi.rtc.dto.CallRecordDTO;
import com.dazi.rtc.dto.CallTokenDTO;
import com.dazi.rtc.dto.GetTokenRequest;
import com.dazi.rtc.dto.StartCallRequest;
import com.dazi.rtc.service.RtcService;
import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/rtc")
@RequiredArgsConstructor
public class RtcController {
    
    private final RtcService rtcService;
    
    /**
     * 获取通话Token
     */
    @PostMapping("/token")
    public Result<CallTokenDTO> getToken(
            HttpServletRequest request,
            @RequestBody GetTokenRequest requestBody) {
        Long userId = (Long) request.getAttribute("currentUserId");
        return rtcService.getToken(userId, requestBody.getRoomId());
    }
    
    /**
     * 发起通话
     */
    @PostMapping("/call")
    public Result<CallRecordDTO> startCall(
            HttpServletRequest request,
            @RequestBody StartCallRequest requestBody) {
        Long callerId = (Long) request.getAttribute("currentUserId");
        return rtcService.startCall(callerId, requestBody.getCalleeId(), requestBody.getCallType());
    }
    
    /**
     * 接受通话
     */
    @PostMapping("/accept/{callId}")
    public Result<Void> acceptCall(
            HttpServletRequest request,
            @PathVariable Long callId) {
        Long userId = (Long) request.getAttribute("currentUserId");
        return rtcService.acceptCall(userId, callId);
    }
    
    /**
     * 拒绝通话
     */
    @PostMapping("/reject/{callId}")
    public Result<Void> rejectCall(
            HttpServletRequest request,
            @PathVariable Long callId) {
        Long userId = (Long) request.getAttribute("currentUserId");
        return rtcService.rejectCall(userId, callId);
    }
    
    /**
     * 结束通话
     */
    @PostMapping("/end/{callId}")
    public Result<Void> endCall(
            HttpServletRequest request,
            @PathVariable Long callId) {
        Long userId = (Long) request.getAttribute("currentUserId");
        return rtcService.endCall(userId, callId);
    }
    
    /**
     * 获取通话记录
     */
    @GetMapping("/records")
    public Result<Page<CallRecordDTO>> getCallRecords(
            HttpServletRequest request,
            @RequestParam(defaultValue = "1") Integer page,
            @RequestParam(defaultValue = "20") Integer pageSize) {
        Long userId = (Long) request.getAttribute("currentUserId");
        return rtcService.getCallRecords(userId, page, pageSize);
    }
}
