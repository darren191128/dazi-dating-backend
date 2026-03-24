package com.dazi.rtc.dto;

import lombok.Data;

@Data
public class CallTokenDTO {
    
    /**
     * 用户签名
     */
    private String userSig;
    
    /**
     * SDK App ID
     */
    private Integer sdkAppId;
    
    /**
     * 用户ID
     */
    private String userId;
    
    /**
     * 房间ID
     */
    private String roomId;
    
    /**
     * Token过期时间（秒）
     */
    private Long expireTime;
}
