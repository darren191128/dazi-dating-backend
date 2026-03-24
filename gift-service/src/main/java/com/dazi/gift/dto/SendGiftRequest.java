package com.dazi.gift.dto;

import jakarta.validation.constraints.NotNull;
import lombok.Data;

@Data
public class SendGiftRequest {
    
    @NotNull(message = "接收者ID不能为空")
    private Long receiverId;
    
    @NotNull(message = "礼物ID不能为空")
    private Long giftId;
    
    private String message;
}
