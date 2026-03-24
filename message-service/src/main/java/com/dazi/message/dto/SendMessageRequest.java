package com.dazi.message.dto;

import lombok.Data;
import java.util.List;

@Data
public class SendMessageRequest {
    
    /**
     * 消息类型：1文本 2图片 3语音
     */
    private Integer messageType;
    
    /**
     * 消息内容
     */
    private String content;
    
    /**
     * 图片URL
     */
    private String imageUrl;
    
    /**
     * 语音URL
     */
    private String voiceUrl;
    
    /**
     * 语音时长(秒)
     */
    private Integer voiceDuration;
    
    /**
     * @成员ID列表
     */
    private List<Long> atUserIds;
}
