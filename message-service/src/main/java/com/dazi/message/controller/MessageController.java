package com.dazi.message.controller;

import com.dazi.common.result.Result;
import com.dazi.message.entity.Conversation;
import com.dazi.message.entity.Message;
import com.dazi.message.service.MessageService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/message")
@RequiredArgsConstructor
public class MessageController {
    
    private final MessageService messageService;
    
    /**
     * 发送消息
     */
    @PostMapping("/send")
    public Result<Long> sendMessage(
            @RequestParam Long senderId,
            @RequestParam Long receiverId,
            @RequestParam String content,
            @RequestParam(defaultValue = "1") Integer type) {
        return messageService.sendMessage(senderId, receiverId, content, type);
    }
    
    /**
     * 获取聊天记录
     */
    @GetMapping("/conversation")
    public Result<List<Message>> getConversation(
            @RequestParam Long userId,
            @RequestParam Long targetId) {
        return messageService.getConversation(userId, targetId);
    }
    
    /**
     * 获取会话列表
     */
    @GetMapping("/conversations/{userId}")
    public Result<List<Conversation>> getConversations(@PathVariable Long userId) {
        return messageService.getConversations(userId);
    }
    
    /**
     * 获取未读消息数
     */
    @GetMapping("/unread/{userId}")
    public Result<Map<String, Object>> getUnreadCount(@PathVariable Long userId) {
        return messageService.getUnreadCount(userId);
    }
}
