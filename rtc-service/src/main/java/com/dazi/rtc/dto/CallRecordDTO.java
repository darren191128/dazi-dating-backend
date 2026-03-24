package com.dazi.rtc.dto;

import lombok.Data;
import java.time.LocalDateTime;

@Data
public class CallRecordDTO {
    
    private Long id;
    
    /**
     * 主叫用户ID
     */
    private Long callerId;
    
    /**
     * 被叫用户ID
     */
    private Long calleeId;
    
    /**
     * 通话类型：1语音 2视频
     */
    private Integer callType;
    
    /**
     * 状态：0待接听 1通话中 2已结束 3已拒绝 4未接听
     */
    private Integer status;
    
    /**
     * 开始时间
     */
    private LocalDateTime startTime;
    
    /**
     * 结束时间
     */
    private LocalDateTime endTime;
    
    /**
     * 通话时长（秒）
     */
    private Integer duration;
    
    /**
     * 房间ID
     */
    private String roomId;
    
    /**
     * 创建时间
     */
    private LocalDateTime createdAt;
}
