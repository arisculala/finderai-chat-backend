package com.finderai.chat.backend.services;

import java.util.List;
import java.util.Random;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import com.finderai.chat.backend.dto.VectorSearchResponseDTO;
import com.finderai.chat.backend.exceptions.AIServiceException;
import com.finderai.chat.backend.exceptions.NoMatchesFoundException;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;

/**
 * Service responsible for providing fallback responses when AI services fail or
 * find no matches.
 */
@Service
@Tag(name = "Fallback Response Service", description = "Generates default responses when AI services fail or find no matches.")
public class FallbackResponseService {
    private static final Logger logger = LoggerFactory.getLogger(FallbackResponseService.class);

    private static final List<String> NO_MATCHES_RESPONSES = List.of(
            "That’s an interesting query! Unfortunately, I don’t have an exact match.",
            "I couldn't find a direct answer.",
            "Hmm... no matches found!",
            "I don’t have that answer yet.");

    private static final List<String> AI_SERVICE_ERROR_RESPONSES = List.of(
            "Oops! Looks like my AI brain had a hiccup.",
            "I ran into a little trouble fetching the answer!",
            "Something went wrong with my knowledge base, but I can still share this!",
            "My AI service is currently having some issues!",
            "I encountered an error, but I’ll still try to help with this!");

    private static final Random RANDOM = new Random();

    /**
     * Generates a fallback response based on the exception type.
     *
     * @param exception The exception that triggered the fallback.
     * @return A default VectorSearchResponseDTO with a random text response.
     */
    @Operation(summary = "Generate a fallback response", description = "Returns a predefined response when AI services fail or find no matches.")
    public VectorSearchResponseDTO generateFallbackResponse(Exception exception) {
        String fallbackText = getRandomText(exception);
        logger.warn("Returning fallback response due to {}: {}", exception.getClass().getSimpleName(), fallbackText);

        return new VectorSearchResponseDTO("fallback", "fallback", fallbackText, null);
    }

    private String getRandomText(Exception exception) {
        if (exception instanceof NoMatchesFoundException) {
            return NO_MATCHES_RESPONSES.get(RANDOM.nextInt(NO_MATCHES_RESPONSES.size()));
        } else if (exception instanceof AIServiceException) {
            return AI_SERVICE_ERROR_RESPONSES.get(RANDOM.nextInt(AI_SERVICE_ERROR_RESPONSES.size()));
        } else {
            return "I'm not sure how to respond to that.";
        }
    }
}
