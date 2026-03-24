package com.dazi.message.websocket;

import com.dazi.message.entity.ChatGroupMessage;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.concurrent.ConcurrentHashMap;

@Slf4j
@Service
public class WebSocketService {
    
    // 存储用户WebSocket会话
    private static final ConcurrentHashMap<Long, WebSocketSession> userSessions = new ConcurrentHashMap<>();
    
    /**
     * 注册用户会话
     */
    public void registerSession(Long userId, WebSocketSession session) {
        userSessions.put(userId, session);
        log.info("用户 {} 已连接WebSocket", userId);
    }
    
    /**
     * 移除用户会话
     */
    public void removeSession(Long userId) {
        userSessions.remove(userId);
        log.info("用户 {} 已断开WebSocket", userId);
    }
    
    /**
     * 发送消息给指定用户
     */
    public void sendMessage(Long userId, Object message) {
        WebSocketSession session = userSessions.get(userId);
        if (session != null && session.isOpen()) {
            session.sendMessage(message);
        }
    }
    
    /**
     * 发送群消息给所有群成员
     */
    public void sendGroupMessage(Long groupId, ChatGroupMessage message) {
        // TODO: 获取群成员列表并发送消息
        // 这里需要查询群成员并逐个发送
        log.info("发送群消息到群 {}: {}", groupId, message.getContent());
    }
    
    /**
     * 广播消息
     */
    public void broadcast(Object message) {
        userSessions.forEach((userId, session) -> {
            if (session.isOpen()) {
                session.sendMessage(message);
            }
        });
    }
    
    /**
     * 检查用户是否在线
     */
    public boolean isUserOnline(Long userId) {
        WebSocketSession session = userSessions.get(userId);
        return session != null && session.isOpen();
    }
}
