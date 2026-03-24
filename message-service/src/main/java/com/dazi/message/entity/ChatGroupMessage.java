package com.dazi.message.entity;

import com.baomidou.mybatisplus.annotation.*;
import lombok.Data;
import java.time.LocalDateTime;

@Data
@TableName("chat_group_message")
public class ChatGroupMessage {
    
    @TableId(value = "id", type = IdType.AUTO)
    private Long id;
    
    /**
     * 群ID
     */
    private Long groupId;
    
    /**
     * 发送者ID
     */
    private Long senderId;
    
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
     * @成员ID列表，逗号分隔
     */
    private String atUserIds;
    
    @TableField(fill = FieldFill.INSERT)
    private LocalDateTime createdAt;
}
