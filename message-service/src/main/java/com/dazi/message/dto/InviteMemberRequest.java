package com.dazi.message.dto;

import lombok.Data;
import java.util.List;

@Data
public class InviteMemberRequest {
    
    /**
     * 邀请成员ID列表
     */
    private List<Long> memberIds;
}
