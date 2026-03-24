package com.dazi.message.websocket;

import org.springframework.web.socket.CloseStatus;
import org.springframework.web.socket.TextMessage;
import org.springframework.web.socket.WebSocketSession;
import org.springframework.web.socket.handler.TextWebSocketHandler;

import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

import java.util.Map;

@Slf4j
@Component
@RequiredArgsConstructor
public class MessageWebSocketHandler extends TextWebSocketHandler {
    
    private final WebSocketService webSocketService;
    private final ObjectMapper objectMapper;
    
    @Override
    public void afterConnectionEstablished(WebSocketSession session) throws Exception {
        Long userId = getUserIdFromSession(session);
        if (userId != null) {
            webSocketService.registerSession(userId, new WebSocketSessionWrapper(session));
        }
    }
    
    @Override
    protected void handleTextMessage(WebSocketSession session, TextMessage message) throws Exception {
        String payload = message.getPayload();
        log.info("收到WebSocket消息: {}", payload);
        
        // 解析消息并处理
        try {
            Map<String, Object> msgData = objectMapper.readValue(payload, Map.class);
            String msgType = (String) msgData.get("type");
            
            switch (msgType) {
                case "ping":
                    session.sendMessage(new TextMessage("{\"type\":\"pong\"}"));
                    break;
                case "read":
                    // 处理已读消息
                    handleReadMessage(msgData);
                    break;
                default:
                    log.warn("未知消息类型: {}", msgType);
            }
        } catch (Exception e) {
            log.error("处理WebSocket消息失败", e);
        }
    }
    
    @Override
    public void afterConnectionClosed(WebSocketSession session, CloseStatus status) throws Exception {
        Long userId = getUserIdFromSession(session);
        if (userId != null) {
            webSocketService.removeSession(userId);
        }
    }
    
    private Long getUserIdFromSession(WebSocketSession session) {
        try {
            String userIdStr = (String) session.getAttributes().get("userId");
            if (userIdStr != null) {
                return Long.valueOf(userIdStr);
            }
        } catch (Exception e) {
            log.error("获取用户ID失败", e);
        }
        return null;
    }
    
    private void handleReadMessage(Map<String, Object> msgData) {
        // 处理消息已读逻辑
    }
}
