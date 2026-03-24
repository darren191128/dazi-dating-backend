package com.dazi.message.service.impl;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.dazi.common.result.Result;
import com.dazi.message.dto.*;
import com.dazi.message.entity.ChatGroup;
import com.dazi.message.entity.ChatGroupMember;
import com.dazi.message.entity.ChatGroupMessage;
import com.dazi.message.repository.ChatGroupMemberRepository;
import com.dazi.message.repository.ChatGroupMessageRepository;
import com.dazi.message.repository.ChatGroupRepository;
import com.dazi.message.service.GroupChatService;
import com.dazi.message.websocket.WebSocketService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.BeanUtils;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.StringUtils;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

@Slf4j
@Service
@RequiredArgsConstructor
public class GroupChatServiceImpl implements GroupChatService {
    
    private final ChatGroupRepository chatGroupRepository;
    private final ChatGroupMemberRepository chatGroupMemberRepository;
    private final ChatGroupMessageRepository chatGroupMessageRepository;
    private final WebSocketService webSocketService;
    private final RedisTemplate<String, Object> redisTemplate;
    
    @Override
    @Transactional
    public Result<GroupDTO> createGroup(Long ownerId, CreateGroupRequest request) {
        // 创建群聊
        ChatGroup group = new ChatGroup();
        group.setName(request.getName());
        group.setAvatar(request.getAvatar());
        group.setOwnerId(ownerId);
        group.setMemberCount(1);
        group.setMaxMemberCount(200);
        group.setStatus(1);
        group.setCreatedAt(LocalDateTime.now());
        group.setUpdatedAt(LocalDateTime.now());
        
        chatGroupRepository.insert(group);
        
        // 添加群主
        ChatGroupMember ownerMember = new ChatGroupMember();
        ownerMember.setGroupId(group.getId());
        ownerMember.setUserId(ownerId);
        ownerMember.setRole(2); // 群主
        ownerMember.setJoinTime(LocalDateTime.now());
        chatGroupMemberRepository.insert(ownerMember);
        
        // 添加其他成员
        if (request.getMemberIds() != null && !request.getMemberIds().isEmpty()) {
            for (Long memberId : request.getMemberIds()) {
                if (!memberId.equals(ownerId)) {
                    ChatGroupMember member = new ChatGroupMember();
                    member.setGroupId(group.getId());
                    member.setUserId(memberId);
                    member.setRole(0); // 普通成员
                    member.setJoinTime(LocalDateTime.now());
                    chatGroupMemberRepository.insert(member);
                }
            }
            // 更新成员数
            group.setMemberCount(1 + request.getMemberIds().size());
            chatGroupRepository.updateById(group);
        }
        
        return Result.success(convertToGroupDTO(group));
    }
    
    @Override
    public Result<List<GroupDTO>> getGroups(Long userId) {
        List<ChatGroup> groups = chatGroupRepository.findByMemberUserId(userId);
        
        List<GroupDTO> dtoList = groups.stream()
            .map(this::convertToGroupDTO)
            .collect(Collectors.toList());
        
        return Result.success(dtoList);
    }
    
    @Override
    public Result<GroupDetailDTO> getGroupDetail(Long userId, Long groupId) {
        ChatGroup group = chatGroupRepository.selectById(groupId);
        if (group == null || group.getStatus() != 1) {
            return Result.notFound();
        }
        
        // 检查用户是否在群中
        ChatGroupMember currentMember = chatGroupMemberRepository.findByGroupIdAndUserId(groupId, userId);
        if (currentMember == null) {
            return Result.forbidden();
        }
        
        GroupDetailDTO dto = new GroupDetailDTO();
        BeanUtils.copyProperties(group, dto);
        dto.setCurrentUserRole(currentMember.getRole());
        
        // 获取成员列表
        List<ChatGroupMember> members = chatGroupMemberRepository.findByGroupId(groupId);
        List<GroupMemberDTO> memberDTOs = members.stream()
            .map(this::convertToGroupMemberDTO)
            .collect(Collectors.toList());
        dto.setMembers(memberDTOs);
        
        return Result.success(dto);
    }
    
    @Override
    @Transactional
    public Result<Void> inviteMember(Long userId, Long groupId, InviteMemberRequest request) {
        ChatGroup group = chatGroupRepository.selectById(groupId);
        if (group == null || group.getStatus() != 1) {
            return Result.notFound();
        }
        
        // 检查邀请者权限
        ChatGroupMember inviter = chatGroupMemberRepository.findByGroupIdAndUserId(groupId, userId);
        if (inviter == null || inviter.getRole() < 1) {
            return Result.forbidden();
        }
        
        // 检查群成员上限
        if (group.getMemberCount() + request.getMemberIds().size() > group.getMaxMemberCount()) {
            return Result.error("群成员数量已达上限");
        }
        
        // 添加新成员
        for (Long memberId : request.getMemberIds()) {
            ChatGroupMember existing = chatGroupMemberRepository.findByGroupIdAndUserId(groupId, memberId);
            if (existing == null) {
                ChatGroupMember member = new ChatGroupMember();
                member.setGroupId(groupId);
                member.setUserId(memberId);
                member.setRole(0);
                member.setJoinTime(LocalDateTime.now());
                chatGroupMemberRepository.insert(member);
                
                chatGroupRepository.incrementMemberCount(groupId);
            }
        }
        
        return Result.success();
    }
    
    @Override
    @Transactional
    public Result<Void> leaveGroup(Long userId, Long groupId) {
        ChatGroup group = chatGroupRepository.selectById(groupId);
        if (group == null || group.getStatus() != 1) {
            return Result.notFound();
        }
        
        ChatGroupMember member = chatGroupMemberRepository.findByGroupIdAndUserId(groupId, userId);
        if (member == null) {
            return Result.error("您不在该群中");
        }
        
        // 群主不能退出，只能解散
        if (member.getRole() == 2) {
            return Result.error("群主无法退出群聊，请转让群主或解散群聊");
        }
        
        chatGroupMemberRepository.deleteById(member.getId());
        chatGroupRepository.decrementMemberCount(groupId);
        
        return Result.success();
    }
    
    @Override
    public Result<Page<GroupMessageDTO>> getGroupMessages(Long userId, Long groupId, Integer page, Integer pageSize) {
        // 检查用户是否在群中
        ChatGroupMember member = chatGroupMemberRepository.findByGroupIdAndUserId(groupId, userId);
        if (member == null) {
            return Result.forbidden();
        }
        
        Page<GroupMessageDTO> resultPage = new Page<>(page, pageSize);
        
        List<ChatGroupMessage> messages = chatGroupMessageRepository.findByGroupIdWithPagination(
            groupId, (page - 1) * pageSize, pageSize);
        Long total = chatGroupMessageRepository.countByGroupId(groupId);
        
        List<GroupMessageDTO> dtoList = messages.stream()
            .map(this::convertToGroupMessageDTO)
            .collect(Collectors.toList());
        
        resultPage.setRecords(dtoList);
        resultPage.setTotal(total);
        
        return Result.success(resultPage);
    }
    
    @Override
    @Transactional
    public Result<GroupMessageDTO> sendGroupMessage(Long userId, Long groupId, SendMessageRequest request) {
        // 检查用户是否在群中
        ChatGroupMember member = chatGroupMemberRepository.findByGroupIdAndUserId(groupId, userId);
        if (member == null) {
            return Result.forbidden();
        }
        
        // 检查是否被禁言
        if (member.getMuteUntil() != null && member.getMuteUntil().isAfter(LocalDateTime.now())) {
            return Result.error("您已被禁言");
        }
        
        // 创建消息
        ChatGroupMessage message = new ChatGroupMessage();
        message.setGroupId(groupId);
        message.setSenderId(userId);
        message.setMessageType(request.getMessageType());
        message.setContent(request.getContent());
        message.setImageUrl(request.getImageUrl());
        message.setVoiceUrl(request.getVoiceUrl());
        message.setVoiceDuration(request.getVoiceDuration());
        
        // 处理@成员
        if (request.getAtUserIds() != null && !request.getAtUserIds().isEmpty()) {
            message.setAtUserIds(String.join(",", request.getAtUserIds().stream()
                .map(String::valueOf).collect(Collectors.toList())));
        }
        
        message.setCreatedAt(LocalDateTime.now());
        chatGroupMessageRepository.insert(message);
        
        // 通过WebSocket发送给群成员
        webSocketService.sendGroupMessage(groupId, message);
        
        return Result.success(convertToGroupMessageDTO(message));
    }
    
    private GroupDTO convertToGroupDTO(ChatGroup group) {
        GroupDTO dto = new GroupDTO();
        BeanUtils.copyProperties(group, dto);
        
        // 获取最后一条消息
        ChatGroupMessage lastMessage = chatGroupMessageRepository.findLatestByGroupId(group.getId());
        if (lastMessage != null) {
            dto.setLastMessage(lastMessage.getContent());
            dto.setLastMessageTime(lastMessage.getCreatedAt());
        }
        
        return dto;
    }
    
    private GroupMemberDTO convertToGroupMemberDTO(ChatGroupMember member) {
        GroupMemberDTO dto = new GroupMemberDTO();
        dto.setId(member.getId());
        dto.setUserId(member.getUserId());
        dto.setGroupNickname(member.getNickname());
        dto.setRole(member.getRole());
        dto.setJoinTime(member.getJoinTime());
        
        // 检查在线状态
        String onlineKey = "user:online:" + member.getUserId();
        dto.setOnline(Boolean.TRUE.equals(redisTemplate.hasKey(onlineKey)));
        
        return dto;
    }
    
    private GroupMessageDTO convertToGroupMessageDTO(ChatGroupMessage message) {
        GroupMessageDTO dto = new GroupMessageDTO();
        BeanUtils.copyProperties(message, dto);
        
        // 解析@成员列表
        if (StringUtils.hasText(message.getAtUserIds())) {
            List<Long> atUserIds = Arrays.stream(message.getAtUserIds().split(","))
                .map(Long::valueOf)
                .collect(Collectors.toList());
            dto.setAtUserIds(atUserIds);
        } else {
            dto.setAtUserIds(new ArrayList<>());
        }
        
        return dto;
    }
}
