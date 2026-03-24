package com.dazi.message.websocket;

import org.springframework.web.socket.WebSocketSession;
import org.springframework.web.socket.TextMessage;

import java.io.IOException;

/**
 * WebSocketSession包装类
 */
public class WebSocketSessionWrapper implements WebSocketSession {
    
    private final WebSocketSession delegate;
    
    public WebSocketSessionWrapper(WebSocketSession delegate) {
        this.delegate = delegate;
    }
    
    @Override
    public String getId() {
        return delegate.getId();
    }
    
    @Override
    public java.net.URI getUri() {
        return delegate.getUri();
    }
    
    @Override
    public org.springframework.http.HttpHeaders getHandshakeHeaders() {
        return delegate.getHandshakeHeaders();
    }
    
    @Override
    public java.util.Map<String, Object> getAttributes() {
        return delegate.getAttributes();
    }
    
    @Override
    public java.security.Principal getPrincipal() {
        return delegate.getPrincipal();
    }
    
    @Override
    public java.net.InetSocketAddress getLocalAddress() {
        return delegate.getLocalAddress();
    }
    
    @Override
    public java.net.InetSocketAddress getRemoteAddress() {
        return delegate.getRemoteAddress();
    }
    
    @Override
    public String getAcceptedProtocol() {
        return delegate.getAcceptedProtocol();
    }
    
    @Override
    public void setTextMessageSizeLimit(int messageSizeLimit) {
        delegate.setTextMessageSizeLimit(messageSizeLimit);
    }
    
    @Override
    public int getTextMessageSizeLimit() {
        return delegate.getTextMessageSizeLimit();
    }
    
    @Override
    public void setBinaryMessageSizeLimit(int messageSizeLimit) {
        delegate.setBinaryMessageSizeLimit(messageSizeLimit);
    }
    
    @Override
    public int getBinaryMessageSizeLimit() {
        return delegate.getBinaryMessageSizeLimit();
    }
    
    @Override
    public java.util.List<org.springframework.web.socket.WebSocketExtension> getExtensions() {
        return delegate.getExtensions();
    }
    
    @Override
    public void sendMessage(org.springframework.web.socket.WebSocketMessage<?> message) throws IOException {
        delegate.sendMessage(message);
    }
    
    public void sendMessage(Object message) {
        try {
            if (message instanceof String) {
                delegate.sendMessage(new TextMessage((String) message));
            } else {
                delegate.sendMessage(new TextMessage(new com.fasterxml.jackson.databind.ObjectMapper().writeValueAsString(message)));
            }
        } catch (IOException e) {
            throw new RuntimeException("发送WebSocket消息失败", e);
        }
    }
    
    @Override
    public boolean isOpen() {
        return delegate.isOpen();
    }
    
    @Override
    public void close() throws IOException {
        delegate.close();
    }
    
    @Override
    public void close(org.springframework.web.socket.CloseStatus status) throws IOException {
        delegate.close(status);
    }
}
