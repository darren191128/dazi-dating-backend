package com.dazi.message.controller;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.dazi.common.result.Result;
import com.dazi.message.dto.*;
import com.dazi.message.entity.Conversation;
import com.dazi.message.entity.Message;
import com.dazi.message.service.GroupChatService;
import com.dazi.message.service.MessageService;
import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/message")
@RequiredArgsConstructor
public class MessageController {
    
    private final MessageService messageService;
    private final GroupChatService groupChatService;
    
    /**
     * 发送消息
     */
    @PostMapping("/conversations/{conversationId}/messages")
    public Result<Long> sendMessage(
            HttpServletRequest request,
            @PathVariable Long conversationId,
            @RequestBody Map<String, Object> params) {
        
        Long senderId = (Long) request.getAttribute("currentUserId");
        Long receiverId = Long.valueOf(params.get("receiverId").toString());
        String content = (String) params.get("content");
        String messageType = (String) params.getOrDefault("messageType", "text");
        Integer type = params.get("type") != null ? Integer.valueOf(params.get("type").toString()) : 1;
        
        return messageService.sendMessage(senderId, receiverId, content, messageType, type);
    }
    
    /**
     * 获取聊天记录
     */
    @GetMapping("/conversations/{conversationId}/messages")
    public Result<List<Message>> getMessages(
            HttpServletRequest request,
            @PathVariable Long conversationId,
            @RequestParam Long targetId) {
        
        Long userId = (Long) request.getAttribute("currentUserId");
        return messageService.getConversation(userId, targetId);
    }
    
    /**
     * 获取会话列表
     */
    @GetMapping("/conversations")
    public Result<List<Conversation>> getConversations(HttpServletRequest request) {
        Long userId = (Long) request.getAttribute("currentUserId");
        return messageService.getConversations(userId);
    }
    
    /**
     * 标记会话已读
     */
    @PostMapping("/conversations/{conversationId}/read")
    public Result<Void> markAsRead(
            HttpServletRequest request,
            @PathVariable Long conversationId) {
        
        Long userId = (Long) request.getAttribute("currentUserId");
        return messageService.markAsRead(userId, conversationId);
    }
    
    /**
     * 获取未读消息数
     */
    @GetMapping("/unread")
    public Result<Map<String, Object>> getUnreadCount(HttpServletRequest request) {
        Long userId = (Long) request.getAttribute("currentUserId");
        return messageService.getUnreadCount(userId);
    }
    
    /**
     * 标记用户消息已读
     */
    @GetMapping("/read/{userId}")
    public Result<Void> markUserMessagesAsRead(
            HttpServletRequest request,
            @PathVariable Long userId) {
        
        Long currentUserId = (Long) request.getAttribute("currentUserId");
        return messageService.markUserMessagesAsRead(currentUserId, userId);
    }
    
    /**
     * 上传语音
     */
    @PostMapping("/upload/voice")
    public Result<Map<String, Object>> uploadVoice(
            HttpServletRequest request,
            @RequestParam("file") MultipartFile file,
            @RequestParam("duration") Integer duration) {
        
        Long userId = (Long) request.getAttribute("currentUserId");
        return messageService.uploadVoice(userId, file, duration);
    }
    
    /**
     * 上传图片
     */
    @PostMapping("/upload/image")
    public Result<Map<String, Object>> uploadImage(
            HttpServletRequest request,
            @RequestParam("file") MultipartFile file) {
        
        Long userId = (Long) request.getAttribute("currentUserId");
        return messageService.uploadImage(userId, file);
    }
    
    // ==================== 群聊功能 ====================
    
    /**
     * 创建群聊
     */
    @PostMapping("/group/create")
    public Result<GroupDTO> createGroup(
            HttpServletRequest request,
            @RequestBody CreateGroupRequest requestBody) {
        Long userId = (Long) request.getAttribute("currentUserId");
        return groupChatService.createGroup(userId, requestBody);
    }
    
    /**
     * 获取群聊列表
     */
    @GetMapping("/groups")
    public Result<List<GroupDTO>> getGroups(HttpServletRequest request) {
        Long userId = (Long) request.getAttribute("currentUserId");
        return groupChatService.getGroups(userId);
    }
    
    /**
     * 获取群详情
     */
    @GetMapping("/group/{groupId}")
    public Result<GroupDetailDTO> getGroupDetail(
            HttpServletRequest request,
            @PathVariable Long groupId) {
        Long userId = (Long) request.getAttribute("currentUserId");
        return groupChatService.getGroupDetail(userId, groupId);
    }
    
    /**
     * 邀请成员
     */
    @PostMapping("/group/{groupId}/invite")
    public Result<Void> inviteMember(
            HttpServletRequest request,
            @PathVariable Long groupId,
            @RequestBody InviteMemberRequest requestBody) {
        Long userId = (Long) request.getAttribute("currentUserId");
        return groupChatService.inviteMember(userId, groupId, requestBody);
    }
    
    /**
     * 退出群聊
     */
    @PostMapping("/group/{groupId}/leave")
    public Result<Void> leaveGroup(
            HttpServletRequest request,
            @PathVariable Long groupId) {
        Long userId = (Long) request.getAttribute("currentUserId");
        return groupChatService.leaveGroup(userId, groupId);
    }
    
    /**
     * 获取群消息
     */
    @GetMapping("/group/{groupId}/messages")
    public Result<Page<GroupMessageDTO>> getGroupMessages(
            HttpServletRequest request,
            @PathVariable Long groupId,
            @RequestParam(defaultValue = "1") Integer page,
            @RequestParam(defaultValue = "20") Integer pageSize) {
        Long userId = (Long) request.getAttribute("currentUserId");
        return groupChatService.getGroupMessages(userId, groupId, page, pageSize);
    }
    
    /**
     * 发送群消息
     */
    @PostMapping("/group/{groupId}/message")
    public Result<GroupMessageDTO> sendGroupMessage(
            HttpServletRequest request,
            @PathVariable Long groupId,
            @RequestBody SendMessageRequest requestBody) {
        Long userId = (Long) request.getAttribute("currentUserId");
        return groupChatService.sendGroupMessage(userId, groupId, requestBody);
    }
}
