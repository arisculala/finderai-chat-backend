package com.finderai.chat.backend.services;

import com.finderai.chat.backend.models.ChatMessage;
import com.finderai.chat.backend.models.ChatMessageDocument;
import com.finderai.chat.backend.repositories.ChatMessageElasticsearchRepository;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

@Service
public class ElasticsearchService {
    private static final Logger logger = LoggerFactory.getLogger(ElasticsearchService.class);

    private final ChatMessageElasticsearchRepository chatMessageElasticsearchRepository;

    public ElasticsearchService(ChatMessageElasticsearchRepository chatMessageElasticsearchRepository) {
        this.chatMessageElasticsearchRepository = chatMessageElasticsearchRepository;
    }

    public void indexMessage(ChatMessage chatMessage) {
        logger.info("Indexing message ID: {}", chatMessage.getId());

        ChatMessageDocument document = ChatMessageDocument.builder()
                .id(chatMessage.getId())
                .botId(chatMessage.getBotId())
                .userId(chatMessage.getUserId())
                .message(chatMessage.getMessage())
                .sender(chatMessage.getSender().name()) // Convert enum to String
                .timestamp(chatMessage.getTimestamp())
                .metadata(chatMessage.getMetadata())
                .build();

        chatMessageElasticsearchRepository.save(document);
        logger.info("Message ID: {} indexed successfully", chatMessage.getId());
    }
}
