package com.campusmarket.campusmarketserver.service;

import com.campusmarket.campusmarketserver.entity.Message;

import java.util.List;
import java.util.Map;

public interface ChatService {
    /** 发消息：存DB + 在线推送 */
    Message send(Long toUserId, String content, Long productId);
    /** 我的会话列表 */
    List<Map<String, Object>> conversations();
    /** 跟某人的历史消息 */
    List<Message> history(Long otherUserId);
}
