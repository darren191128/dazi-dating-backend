package com.dazi.message.service;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.dazi.common.result.Result;
import com.dazi.message.dto.*;

import java.util.List;

public interface GroupChatService {
    
    /**
     * 创建群聊
     */
    Result<GroupDTO> createGroup(Long ownerId, CreateGroupRequest request);
    
    /**
     * 获取群聊列表
     */
    Result<List<GroupDTO>> getGroups(Long userId);
    
    /**
     * 获取群详情
     */
    Result<GroupDetailDTO> getGroupDetail(Long userId, Long groupId);
    
    /**
     * 邀请成员
     */
    Result<Void> inviteMember(Long userId, Long groupId, InviteMemberRequest request);
    
    /**
     * 退出群聊
     */
    Result<Void> leaveGroup(Long userId, Long groupId);
    
    /**
     * 获取群消息
     */
    Result<Page<GroupMessageDTO>> getGroupMessages(Long userId, Long groupId, Integer page, Integer pageSize);
    
    /**
     * 发送群消息
     */
    Result<GroupMessageDTO> sendGroupMessage(Long userId, Long groupId, SendMessageRequest request);
}
