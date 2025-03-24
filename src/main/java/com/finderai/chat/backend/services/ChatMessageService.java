package com.finderai.chat.backend.services;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;

import com.finderai.chat.backend.dto.ChatHistoryResponseDTO;
import com.finderai.chat.backend.dto.SendChatMessageDTO;
import com.finderai.chat.backend.models.ChatMessage;
import com.finderai.chat.backend.repositories.ChatMessageRepository;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;

/**
 * Service responsible for handling chat messages, storing them in a database,
 * indexing them in Elasticsearch, and generating bot responses.
 */
@Service
@Tag(name = "Chat Message Service", description = "Handles chat message processing, storage, and retrieval.")
public class ChatMessageService {
    private static final Logger logger = LoggerFactory.getLogger(ChatMessageService.class);

    private final ReplierService replierService;
    private final ChatMessageRepository chatMessageRepository;
    private final ElasticsearchService elasticsearchService;

    public ChatMessageService(ReplierService replierService, ChatMessageRepository chatMessageRepository,
            ElasticsearchService elasticsearchService) {
        this.replierService = replierService;
        this.chatMessageRepository = chatMessageRepository;
        this.elasticsearchService = elasticsearchService;
    }

    /**
     * Processes a chat message from a user, stores it, generates a bot response,
     * and indexes both messages in Elasticsearch.
     *
     * @param messageDTO The chat message request containing user details, message
     *                   text, and AI type.
     * @return The bot's response message.
     */
    @Operation(summary = "Send a chat message", description = "Processes a user message, generates a bot reply, and stores both messages.")
    public ChatMessage chatMessage(SendChatMessageDTO messageDTO) {
        logger.debug("Processing chat message from bot: {}, user: {}", messageDTO.getBotId(), messageDTO.getUserId());

        // Save user message
        ChatMessage userMessage = ChatMessage.builder()
                .botId(messageDTO.getBotId())
                .userId(messageDTO.getUserId())
                .message(messageDTO.getMessage())
                .sender(ChatMessage.Sender.USER)
                .timestamp(null)
                .metadata(messageDTO.getMetadata())
                .build();
        chatMessageRepository.save(userMessage);

        // Index user message in Elasticsearch
        elasticsearchService.indexMessage(userMessage);

        // Generate bot response using ReplierService
        ChatMessage botMessage = replierService.generateReply(
                messageDTO.getBotId(),
                messageDTO.getUserId(),
                messageDTO.getMessage(),
                messageDTO.getAiType(),
                messageDTO.getLimit());
        chatMessageRepository.save(botMessage);

        // Index bot response in Elasticsearch
        elasticsearchService.indexMessage(botMessage);

        return botMessage;
    }

    /**
     * Retrieves paginated chat history for a user.
     *
     * @param userId The user's ID.
     * @param page   The page number (0-based).
     * @param size   The number of messages per page.
     * @return A paginated response containing chat messages and metadata.
     */
    @SuppressWarnings("unchecked")
    @Operation(summary = "Get chat history for a user", description = "Retrieves paginated chat messages for a specific user.")
    public ChatHistoryResponseDTO getUserChatHistory(String userId, int page, int size) {
        PageRequest pageable = PageRequest.of(page, size);
        Page<ChatMessage> chatPage = (Page<ChatMessage>) chatMessageRepository.findByUserIdOrderByTimestampAsc(userId,
                pageable);

        return new ChatHistoryResponseDTO(
                chatPage.getContent(),
                chatPage.getNumber(),
                chatPage.getSize(),
                chatPage.getTotalPages(),
                chatPage.getTotalElements());
    }

    /**
     * Retrieves paginated chat history between a specific bot and a user.
     *
     * @param botId  The bot's ID.
     * @param userId The user's ID.
     * @param page   The page number (0-based).
     * @param size   The number of messages per page.
     * @return A paginated response containing chat messages and metadata.
     */
    @SuppressWarnings("unchecked")
    @Operation(summary = "Get chat history for a bot and user", description = "Retrieves paginated chat messages exchanged between a bot and a user.")
    public ChatHistoryResponseDTO getUserBotChatHistory(String botId, String userId, int page, int size) {
        PageRequest pageable = PageRequest.of(page, size);
        Page<ChatMessage> chatPage = (Page<ChatMessage>) chatMessageRepository.findByBotIdAndUserIdOrderByTimestampAsc(
                botId, userId,
                pageable);

        return new ChatHistoryResponseDTO(
                chatPage.getContent(),
                chatPage.getNumber(),
                chatPage.getSize(),
                chatPage.getTotalPages(),
                chatPage.getTotalElements());
    }
}
