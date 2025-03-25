package com.finderai.chat.backend.controllers;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.finderai.chat.backend.dto.ChatHistoryResponseDTO;
import com.finderai.chat.backend.dto.SendChatMessageDTO;
import com.finderai.chat.backend.models.ChatMessage;
import com.finderai.chat.backend.models.ChatMessage.Sender;
import com.finderai.chat.backend.services.ChatMessageService;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;

@RestController
@RequestMapping("/api/v1/chat")
@RequiredArgsConstructor
@Tag(name = "Chat Message API", description = "API for managing chat messages")
public class ChatController {
    private static final Logger logger = LoggerFactory.getLogger(ChatController.class);

    private final ChatMessageService chatMessageService;

    @PostMapping
    @Operation(summary = "Send a chat message", description = "Processes a user message and generates a response.")
    public ChatMessage chatMessage(@RequestBody SendChatMessageDTO chatMessage) {
        logger.info("Calling POST /chat endpoint");
        return chatMessageService.chatMessage(chatMessage);
    }

    @GetMapping("/users/{userId}/history")
    @Operation(summary = "Get user chat history", description = "Retrieves all chat messages for a specific user with pagination.")
    public ChatHistoryResponseDTO getUserChatHistory(
            @PathVariable String userId,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "20") int size,
            @RequestParam(required = false) String sender) {
        logger.info("Calling GET /chat/{userId} endpoint with page={} and size={}", page, size);
        ChatMessage.Sender senderEnum = (sender == null || sender.isBlank()) ? null
                : ChatMessage.Sender.valueOf(sender.toUpperCase());
        return chatMessageService.getUserChatHistory(userId, page, size, senderEnum);
    }

    @GetMapping("/bots/{botId}/users/{userId}/history")
    @Operation(summary = "Get chat history by bot and user", description = "Retrieves chat messages between a bot and a user with pagination.")
    public ChatHistoryResponseDTO getUserBotChatHistory(
            @PathVariable String botId,
            @PathVariable String userId,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "20") int size,
            @RequestParam(required = false) String sender) {
        logger.info("Calling GET /chat/{botId}/{userId} endpoint with page={} and size={}", page, size);
        ChatMessage.Sender senderEnum = (sender == null || sender.isBlank()) ? null
                : ChatMessage.Sender.valueOf(sender.toUpperCase());
        return chatMessageService.getUserBotChatHistory(botId, userId, page, size, senderEnum);
    }
}
