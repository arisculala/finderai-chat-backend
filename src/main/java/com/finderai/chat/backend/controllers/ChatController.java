package com.finderai.chat.backend.controllers;

import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.finderai.chat.backend.dto.SendChatMessageDTO;
import com.finderai.chat.backend.models.ChatMessage;
import com.finderai.chat.backend.services.ChatMessageService;

import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;

@RestController
@RequestMapping("/api/v1/chat")
@RequiredArgsConstructor
@Tag(name = "Chat Message API", description = "API for chat messages")
public class ChatController {
    private static final Logger logger = LoggerFactory.getLogger(ChatController.class);

    private final ChatMessageService chatMessageService;

    @PostMapping
    public ChatMessage chatMessage(@RequestBody SendChatMessageDTO chatMessage) {
        return chatMessageService.chatMessage(chatMessage);
    }

    @GetMapping("/{botId}/{userId}")
    public List<ChatMessage> getUserChatHistory(@PathVariable String botId, @PathVariable String userId) {
        return chatMessageService.getUserBotChatHistory(botId, userId);
    }

    @GetMapping("/{userId}")
    public List<ChatMessage> getUserChatHistory(@PathVariable String userId) {
        return chatMessageService.getUserChatHistory(userId);
    }
}
