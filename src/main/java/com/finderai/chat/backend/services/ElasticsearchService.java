package com.finderai.chat.backend.services;

import com.finderai.chat.backend.models.ChatMessage;
import com.finderai.chat.backend.models.ChatMessageDocument;
import com.finderai.chat.backend.repositories.ChatMessageElasticsearchRepository;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;

/**
 * Service responsible for indexing chat messages in Elasticsearch.
 */
@Service
@Tag(name = "Elasticsearch Service", description = "Handles indexing and retrieval of chat messages in Elasticsearch.")
public class ElasticsearchService {
    private static final Logger logger = LoggerFactory.getLogger(ElasticsearchService.class);

    private final ChatMessageElasticsearchRepository chatMessageElasticsearchRepository;

    public ElasticsearchService(ChatMessageElasticsearchRepository chatMessageElasticsearchRepository) {
        this.chatMessageElasticsearchRepository = chatMessageElasticsearchRepository;
    }

    /**
     * Indexes a chat message into Elasticsearch.
     *
     * @param chatMessage The chat message to be indexed.
     */
    @Operation(summary = "Index a chat message", description = "Stores a chat message in Elasticsearch for fast retrieval and search.")
    public void indexMessage(ChatMessage chatMessage) {
        logger.info("Indexing message ID: {}", chatMessage.getId());

        ChatMessageDocument document = ChatMessageDocument.builder()
                .id(chatMessage.getId())
                .botId(chatMessage.getBotId())
                .userId(chatMessage.getUserId())
                .message(chatMessage.getMessage())
                .sender(chatMessage.getSender())
                .timestamp(chatMessage.getTimestamp())
                .metadata(chatMessage.getMetadata())
                .build();

        chatMessageElasticsearchRepository.save(document);
        logger.info("Message ID: {} indexed successfully", chatMessage.getId());
    }
}
