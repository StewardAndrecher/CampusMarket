package com.campusmarket.campusmarketserver.chat;

import com.campusmarket.campusmarketserver.util.JwtUtil;
import org.springframework.stereotype.Component;
import org.springframework.web.socket.CloseStatus;
import org.springframework.web.socket.TextMessage;
import org.springframework.web.socket.WebSocketSession;
import org.springframework.web.socket.handler.TextWebSocketHandler;

import java.net.URI;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

/**
 * WebSocket 聊天处理器：维护 userId -> Session 的在线映射。
 * 连接时 URL 带 ?token=xxx，握手后解析出 userId 绑定。
 */
@Component
public class ChatWebSocketHandler extends TextWebSocketHandler {

    // 在线用户：userId -> session
    public static final Map<Long, WebSocketSession> ONLINE = new ConcurrentHashMap<>();

    @Override
    public void afterConnectionEstablished(WebSocketSession session) {
        URI uri = session.getUri();
        if (uri == null) return;
        String query = uri.getQuery();
        Long userId = null;
        if (query != null) {
            for (String kv : query.split("&")) {
                if (kv.startsWith("token=")) {
                    try {
                        userId = JwtUtil.parseUserId(kv.substring(6));
                    } catch (Exception ignored) {}
                }
            }
        }
        if (userId == null) {
            try { session.close(CloseStatus.POLICY_VIOLATION); } catch (Exception ignored) {}
            return;
        }
        session.getAttributes().put("userId", userId);
        ONLINE.put(userId, session);
    }

    @Override
    public void afterConnectionClosed(WebSocketSession session, CloseStatus status) {
        Long userId = (Long) session.getAttributes().get("userId");
        if (userId != null) ONLINE.remove(userId);
    }

    /** 主动给某个在线用户推消息 */
    public static void sendToUser(Long userId, String json) {
        WebSocketSession s = ONLINE.get(userId);
        if (s != null && s.isOpen()) {
            try {
                synchronized (s) {
                    s.sendMessage(new TextMessage(json));
                }
            } catch (Exception ignored) {}
        }
    }
}
