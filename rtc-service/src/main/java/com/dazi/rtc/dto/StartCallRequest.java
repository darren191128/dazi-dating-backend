package com.dazi.rtc.dto;

import lombok.Data;

@Data
public class StartCallRequest {
    
    /**
     * 被叫用户ID
     */
    private Long calleeId;
    
    /**
     * 通话类型：1语音 2视频
     */
    private Integer callType;
}
