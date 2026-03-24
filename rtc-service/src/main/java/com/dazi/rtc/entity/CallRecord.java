package com.dazi.rtc.entity;

import com.baomidou.mybatisplus.annotation.*;
import lombok.Data;
import java.time.LocalDateTime;

@Data
@TableName("call_record")
public class CallRecord {
    
    @TableId(value = "id", type = IdType.AUTO)
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
    
    @TableField(fill = FieldFill.INSERT)
    private LocalDateTime createdAt;
    
    @TableField(fill = FieldFill.INSERT_UPDATE)
    private LocalDateTime updatedAt;
}
