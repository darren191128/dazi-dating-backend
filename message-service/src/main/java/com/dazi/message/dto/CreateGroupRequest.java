package com.dazi.message.dto;

import lombok.Data;
import java.util.List;

@Data
public class CreateGroupRequest {
    
    /**
     * 群名称
     */
    private String name;
    
    /**
     * 群头像
     */
    private String avatar;
    
    /**
     * 初始成员ID列表
     */
    private List<Long> memberIds;
}
