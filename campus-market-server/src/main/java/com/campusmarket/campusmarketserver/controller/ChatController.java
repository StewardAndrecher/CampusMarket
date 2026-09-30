package com.campusmarket.campusmarketserver.controller;

import com.campusmarket.campusmarketserver.common.BusinessException;
import com.campusmarket.campusmarketserver.entity.Message;
import com.campusmarket.campusmarketserver.service.ChatService;
import com.campusmarket.common.Result;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/chat")
public class ChatController {

    private final ChatService chatService;
    public ChatController(ChatService chatService) { this.chatService = chatService; }

    @PostMapping("/send")
    public Result<Message> send(@RequestBody Map<String, Object> body) {
        Long toUserId = Long.valueOf(body.get("toUserId").toString());
        String content = (String) body.get("content");
        Long productId = body.get("productId") != null ? Long.valueOf(body.get("productId").toString()) : null;
        return Result.ok(chatService.send(toUserId, content, productId));
    }

    @GetMapping("/conversations")
    public Result<List<Map<String, Object>>> conversations() {
        return Result.ok(chatService.conversations());
    }

    @GetMapping("/history/{otherUserId}")
    public Result<List<Message>> history(@PathVariable Long otherUserId) {
        return Result.ok(chatService.history(otherUserId));
    }
}
