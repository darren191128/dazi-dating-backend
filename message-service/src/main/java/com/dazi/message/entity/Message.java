package com.dazi.message.entity;

import com.baomidou.mybatisplus.annotation.*;
import lombok.Data;
import java.time.LocalDateTime;

@Data
@TableName("message")
public class Message {
    
    @TableId(value = "id", type = IdType.AUTO)
    private Long id;
    
    private Long senderId;
    
    private Long receiverId;
    
    private Long activityId;
    
    /**
     * 消息类型: 1-系统消息, 2-活动消息
     */
    private Integer type;
    
    /**
     * 消息内容类型: text-文本, voice-语音, image-图片
     */
    private String messageType;
    
    private String content;
    
    private String imageUrl;
    
    /**
     * 语音URL
     */
    private String voiceUrl;
    
    /**
     * 语音时长(秒)
     */
    private Integer voiceDuration;
    
    private Integer status;
    
    private LocalDateTime readTime;
    
    @TableField(fill = FieldFill.INSERT)
    private LocalDateTime createTime;
    
    @TableField(fill = FieldFill.INSERT_UPDATE)
    private LocalDateTime updateTime;
}
