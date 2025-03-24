package com.finderai.chat.backend.services;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Map;
import java.util.Optional;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import com.finderai.chat.backend.dto.VectorSearchResponseDTO;
import com.finderai.chat.backend.enums.AIType;
import com.finderai.chat.backend.exceptions.AIServiceException;
import com.finderai.chat.backend.exceptions.NoMatchesFoundException;
import com.finderai.chat.backend.models.ChatMessage;

@Service
public class ReplierService {
    private static final Logger logger = LoggerFactory.getLogger(FinderAIService.class);

    private final List<AIService> aiServices;
    private final FinderAIService finderAIService; // Default AI service
    private final FallbackResponseService fallbackResponseService;

    public ReplierService(List<AIService> aiServices, FinderAIService finderAIService,
            FallbackResponseService fallbackResponseService) {
        this.aiServices = aiServices;
        this.finderAIService = finderAIService;
        this.fallbackResponseService = fallbackResponseService;
    }

    /**
     * Generates a chatbot response based on the user's message and selected AI
     * service.
     *
     * @param botId   The chatbot ID.
     * @param userId  The user ID.
     * @param message The user message.
     * @param aiType  The AI service type (e.g., "finderai", "openai").
     * @return A chatbot response message.
     */
    public ChatMessage generateReply(String botId, String userId, String message, AIType aiType, int limit) {
        logger.info("Calling generateReply mtehod");

        // Use "finderai" as default if aiType is null
        String selectedAIType = (aiType != null) ? aiType.getType() : AIType.FINDER_AI.getType();

        // Select AI service dynamically based on aiType
        Optional<AIService> selectedService = aiServices.stream()
                .filter(service -> service.getClass().getSimpleName().toLowerCase()
                        .contains(selectedAIType.toLowerCase()))
                .findFirst();

        AIService aiService = selectedService.orElse(finderAIService); // Default to FinderAIService

        if (selectedService.isEmpty()) {
            logger.info("AI service '{}' not found. Falling back to FinderAIService.", selectedAIType);
        }

        List<VectorSearchResponseDTO> matches;
        try {
            // Perform AI search
            matches = aiService.searchSimilarTexts(userId, message, selectedAIType, limit);
        } catch (NoMatchesFoundException | AIServiceException e) {
            logger.warn("Error occurred: {}. Using fallback response.", e.getMessage());
            matches = List.of(fallbackResponseService.generateFallbackResponse(e));
        }

        // Prepare bot response (first match)
        String botResponse = matches.isEmpty() ? "I'm not sure how to respond to that." : matches.get(0).getText();

        // Construct and save bot reply with full matches in metadata
        ChatMessage replyMessage = ChatMessage.builder()
                .botId(botId)
                .userId(userId)
                .message(botResponse)
                .sender(ChatMessage.Sender.BOT)
                .timestamp(LocalDateTime.now())
                .metadata(Map.of(
                        "source", selectedAIType,
                        "matches", matches)) // Store AI type and matches
                .build();

        return replyMessage;
    }

}
