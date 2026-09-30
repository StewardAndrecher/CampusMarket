package com.campusmarket.campusmarketserver.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.campusmarket.campusmarketserver.chat.ChatWebSocketHandler;
import com.campusmarket.campusmarketserver.common.BusinessException;
import com.campusmarket.campusmarketserver.entity.Conversation;
import com.campusmarket.campusmarketserver.entity.Message;
import com.campusmarket.campusmarketserver.entity.User;
import com.campusmarket.campusmarketserver.mapper.ConversationMapper;
import com.campusmarket.campusmarketserver.mapper.MessageMapper;
import com.campusmarket.campusmarketserver.mapper.UserMapper;
import com.campusmarket.campusmarketserver.service.ChatService;
import com.campusmarket.campusmarketserver.util.UserContext;
import org.springframework.stereotype.Service;

import java.util.*;

@Service
public class ChatServiceImpl implements ChatService {

    private final ConversationMapper conversationMapper;
    private final MessageMapper messageMapper;
    private final UserMapper userMapper;

    public ChatServiceImpl(ConversationMapper conversationMapper, MessageMapper messageMapper, UserMapper userMapper) {
        this.conversationMapper = conversationMapper;
        this.messageMapper = messageMapper;
        this.userMapper = userMapper;
    }

    @Override
    public Message send(Long toUserId, String content, Long productId) {
        Long me = UserContext.getUserId();
        if (toUserId.equals(me)) throw new BusinessException("不能给自己发消息");
        if (content == null || content.isBlank()) throw new BusinessException("消息不能为空");

        long u1 = Math.min(me, toUserId);
        long u2 = Math.max(me, toUserId);

        Conversation conv = conversationMapper.selectOne(new LambdaQueryWrapper<Conversation>()
                .eq(Conversation::getUser1Id, u1).eq(Conversation::getUser2Id, u2));
        if (conv == null) {
            conv = new Conversation();
            conv.setUser1Id(u1);
            conv.setUser2Id(u2);
            conv.setProductId(productId);
            conv.setLastMessage(content);
            conversationMapper.insert(conv);
        } else {
            conv.setLastMessage(content);
            conversationMapper.updateById(conv);
        }

        Message m = new Message();
        m.setConversationId(conv.getId());
        m.setSenderId(me);
        m.setContent(content);
        m.setType(0);
        m.setIsRead(0);
        messageMapper.insert(m);

        // 对方在线就推
        Map<String, Object> push = new HashMap<>();
        push.put("type", "message");
        push.put("fromUserId", me);
        push.put("content", content);
        push.put("createTime", m.getCreateTime());
        ChatWebSocketHandler.sendToUser(toUserId, toJson(push));
        return m;
    }

    @Override
    public List<Map<String, Object>> conversations() {
        Long me = UserContext.getUserId();
        List<Conversation> list = conversationMapper.selectList(new LambdaQueryWrapper<Conversation>()
                .eq(Conversation::getUser1Id, me).or().eq(Conversation::getUser2Id, me)
                .orderByDesc(Conversation::getUpdateTime));
        List<Map<String, Object>> result = new ArrayList<>();
        for (Conversation c : list) {
            Long otherId = c.getUser1Id().equals(me) ? c.getUser2Id() : c.getUser1Id();
            User other = userMapper.selectById(otherId);
            Map<String, Object> m = new HashMap<>();
            m.put("conversationId", c.getId());
            m.put("otherUserId", otherId);
            m.put("otherNickname", other != null ? other.getNickname() : "未知");
            m.put("otherAvatar", other != null ? other.getAvatar() : null);
            m.put("lastMessage", c.getLastMessage());
            m.put("updateTime", c.getUpdateTime());
            result.add(m);
        }
        return result;
    }

    @Override
    public List<Message> history(Long otherUserId) {
        Long me = UserContext.getUserId();
        long u1 = Math.min(me, otherUserId);
        long u2 = Math.max(me, otherUserId);
        Conversation conv = conversationMapper.selectOne(new LambdaQueryWrapper<Conversation>()
                .eq(Conversation::getUser1Id, u1).eq(Conversation::getUser2Id, u2));
        if (conv == null) return Collections.emptyList();
        return messageMapper.selectList(new LambdaQueryWrapper<Message>()
                .eq(Message::getConversationId, conv.getId()).orderByAsc(Message::getId));
    }

    private String toJson(Map<String, Object> m) {
        try {
            return new com.fasterxml.jackson.databind.ObjectMapper().writeValueAsString(m);
        } catch (Exception e) {
            return "{}";
        }
    }
}
