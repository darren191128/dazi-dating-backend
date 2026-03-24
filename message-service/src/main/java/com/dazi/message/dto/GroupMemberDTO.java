package com.dazi.message.dto;

import lombok.Data;
import java.time.LocalDateTime;

@Data
public class GroupMemberDTO {
    
    private Long id;
    
    /**
     * 用户ID
     */
    private Long userId;
    
    /**
     * 用户昵称
     */
    private String nickname;
    
    /**
     * 用户头像
     */
    private String avatar;
    
    /**
     * 群内昵称
     */
    private String groupNickname;
    
    /**
     * 角色：0成员 1管理员 2群主
     */
    private Integer role;
    
    /**
     * 是否在线
     */
    private Boolean online;
    
    /**
     * 加入时间
     */
    private LocalDateTime joinTime;
}
