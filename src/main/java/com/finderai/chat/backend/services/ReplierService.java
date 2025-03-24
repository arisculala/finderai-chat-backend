package com.finderai.chat.backend.services;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import com.finderai.chat.backend.dto.VectorSearchResponseDTO;
import com.finderai.chat.backend.enums.AIType;
import com.finderai.chat.backend.exceptions.AIServiceException;
import com.finderai.chat.backend.exceptions.NoMatchesFoundException;
import com.finderai.chat.backend.models.ChatMessage;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;

/**
 * Service responsible for generating chatbot replies using AI services.
 */
@Service
@Tag(name = "Replier Service", description = "Handles chatbot responses by querying different AI services.")
public class ReplierService {
    private static final Logger logger = LoggerFactory.getLogger(ReplierService.class);

    private final List<AIService> aiServices;
    private final FinderAIService finderAIService;
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
     * @param aiType  The AI service type.
     * @return A chatbot response message.
     */
    @Operation(summary = "Generate chatbot response", description = "Queries an AI service to generate a response for the user.")
    public ChatMessage generateReply(String botId, String userId, String message, AIType aiType, int limit) {
        logger.info("Generating chatbot reply...");

        String selectedAIType = (aiType != null) ? aiType.getType() : AIType.FINDER_AI.getType();

        AIService aiService = aiServices.stream()
                .filter(service -> service.getClass().getSimpleName().toLowerCase()
                        .contains(selectedAIType.toLowerCase()))
                .findFirst()
                .orElse(finderAIService);

        List<VectorSearchResponseDTO> matches;
        try {
            matches = aiService.searchSimilarTexts(userId, message, selectedAIType, limit);
        } catch (NoMatchesFoundException | AIServiceException e) {
            logger.warn("Error occurred: {}. Using fallback response.", e.getMessage());
            matches = List.of(fallbackResponseService.generateFallbackResponse(e));
        }

        return ChatMessage.builder()
                .botId(botId)
                .userId(userId)
                .message(matches.isEmpty() ? "I'm not sure how to respond to that." : matches.get(0).getText())
                .sender(ChatMessage.Sender.BOT)
                .timestamp(LocalDateTime.now())
                .metadata(Map.of("source", selectedAIType, "matches", matches))
                .build();
    }
}
