package com.dazi.message.entity;

import com.baomidou.mybatisplus.annotation.*;
import lombok.Data;
import java.time.LocalDateTime;

@Data
@TableName("chat_group_member")
public class ChatGroupMember {
    
    @TableId(value = "id", type = IdType.AUTO)
    private Long id;
    
    /**
     * 群ID
     */
    private Long groupId;
    
    /**
     * 用户ID
     */
    private Long userId;
    
    /**
     * 群内昵称
     */
    private String nickname;
    
    /**
     * 角色：0成员 1管理员 2群主
     */
    private Integer role;
    
    /**
     * 禁言截止时间
     */
    private LocalDateTime muteUntil;
    
    /**
     * 加入时间
     */
    @TableField(fill = FieldFill.INSERT)
    private LocalDateTime joinTime;
}
