package com.dazi.message.entity;

import com.baomidou.mybatisplus.annotation.*;
import lombok.Data;
import java.time.LocalDateTime;

@Data
@TableName("chat_group")
public class ChatGroup {
    
    @TableId(value = "id", type = IdType.AUTO)
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
     * 状态：0解散 1正常
     */
    private Integer status;
    
    @TableField(fill = FieldFill.INSERT)
    private LocalDateTime createdAt;
    
    @TableField(fill = FieldFill.INSERT_UPDATE)
    private LocalDateTime updatedAt;
}
