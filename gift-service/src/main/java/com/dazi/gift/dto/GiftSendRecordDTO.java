package com.dazi.gift.dto;

import lombok.Data;

import java.time.LocalDateTime;

@Data
public class GiftSendRecordDTO {
    
    private Long id;
    private Long senderId;
    private Long receiverId;
    private Long giftId;
    private String giftName;
    private String giftIcon;
    private Integer price;
    private String message;
    private LocalDateTime createdAt;
    
    // 发送者信息（冗余）
    private String senderNickname;
    private String senderAvatar;
    
    // 接收者信息（冗余）
    private String receiverNickname;
    private String receiverAvatar;
}
