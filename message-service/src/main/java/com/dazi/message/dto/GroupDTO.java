package com.dazi.message.dto;

import lombok.Data;
import java.time.LocalDateTime;

@Data
public class GroupDTO {
    
    private Long id;
    
    /**
     * 群名称
     */
    private String name;
    
    /**
     * 群头像
     */
    private String avatar;
    
    /**
     * 群主ID
     */
    private Long ownerId;
    
    /**
     * 成员数量
     */
    private Integer memberCount;
    
    /**
     * 最大成员数
     */
    private Integer maxMemberCount;
    
    /**
     * 群公告
     */
    private String notice;
    
    /**
     * 未读消息数
     */
    private Integer unreadCount;
    
    /**
     * 最后一条消息
     */
    private String lastMessage;
    
    /**
     * 最后消息时间
     */
    private LocalDateTime lastMessageTime;
    
    /**
     * 创建时间
     */
    private LocalDateTime createdAt;
}
