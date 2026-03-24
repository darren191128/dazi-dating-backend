package com.dazi.message.dto;

import lombok.Data;
import java.time.LocalDateTime;
import java.util.List;

@Data
public class GroupDetailDTO {
    
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
     * 成员列表
     */
    private List<GroupMemberDTO> members;
    
    /**
     * 当前用户角色
     */
    private Integer currentUserRole;
    
    /**
     * 创建时间
     */
    private LocalDateTime createdAt;
}
