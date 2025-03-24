package com.finderai.chat.backend.services;

import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import com.finderai.chat.backend.dto.SendChatMessageDTO;
import com.finderai.chat.backend.models.ChatMessage;
import com.finderai.chat.backend.repositories.ChatMessageRepository;

@Service
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

        // Call ReplierService to generate bot response message
        ChatMessage botMessage = replierService.generateReply(messageDTO.getBotId(), messageDTO.getUserId(),
                messageDTO.getMessage(), messageDTO.getAiType(), messageDTO.getLimit());
        chatMessageRepository.save(botMessage);

        // Index bot response in Elasticsearch
        elasticsearchService.indexMessage(botMessage);

        return botMessage;
    }

    public List<ChatMessage> getUserChatHistory(String userId) {
        return chatMessageRepository.findByUserIdOrderByTimestampAsc(userId);
    }

    public List<ChatMessage> getUserBotChatHistory(String botId, String userId) {
        return chatMessageRepository.findByBotIdAndUserIdOrderByTimestampAsc(botId, userId);
    }
}
