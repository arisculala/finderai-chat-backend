package com.finderai.chat.backend.services;

import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestClientException;
import org.springframework.web.client.RestTemplate;

import com.fasterxml.jackson.databind.JsonNode;
import com.finderai.chat.backend.dto.VectorSearchResponseDTO;
import com.finderai.chat.backend.exceptions.AIServiceException;
import com.finderai.chat.backend.exceptions.NoMatchesFoundException;

/**
 * Service responsible for searching similar text responses using OpenAI.
 */
@Service
public class OpenAIService implements AIService {
    private static final Logger logger = LoggerFactory.getLogger(OpenAIService.class);

    // OpenAI API configuration values (loaded from application properties)
    @Value("${ai.provider.openai.api.url}")
    private String apiUrl;

    @Value("${ai.provider.openai.api.key}")
    private String apiKey;

    @Value("${ai.provider.openai.api.model}")
    private String apiModel;

    @Value("${ai.provider.openai.api.limit}")
    private int apiLimit;

    private final RestTemplate restTemplate;

    public OpenAIService(RestTemplate restTemplate) {
        this.restTemplate = restTemplate;
    }

    /**
     * Searches for similar texts using OpenAI's chat completion API.
     *
     * @param query    The user input text.
     * @param provider The AI provider name (e.g., "openai").
     * @param model    The AI model to use (fallbacks to default if null).
     * @param limit    The number of results to return (fallbacks to default if 0).
     * @return A list of search results, or a fallback message if no results are
     *         found.
     */
    @Override
    public List<VectorSearchResponseDTO> searchSimilarTexts(String query, String provider, String model, int limit) {
        logger.debug("Calling OpenAIService searchSimilarTexts method with query: {}", query);

        // Use default values if parameters are not provided
        String defaultModel = (model != null) ? model : apiModel;
        int defaultLimit = (limit == 0) ? apiLimit : limit;

        // Construct OpenAI API request payload
        String requestBody = """
                {
                    "model": "%s",
                    "messages": [{"role": "user", "content": "%s"}],
                    "max_tokens": 100
                }
                """.formatted(defaultModel, query);

        // Prepare HTTP headers
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);
        headers.setBearerAuth(apiKey); // OpenAI requires Bearer token authentication

        // Create HTTP entity with request body and headers
        HttpEntity<String> entity = new HttpEntity<>(requestBody, headers);

        try {
            // Make an HTTP request to OpenAI's API
            ResponseEntity<JsonNode> response = restTemplate.exchange(
                    apiUrl + "/v1/chat/completions", // OpenAI endpoint for chat models
                    HttpMethod.POST,
                    entity,
                    JsonNode.class);

            // Extract response body
            JsonNode responseBody = response.getBody();

            // Validate response structure
            if (responseBody == null || !responseBody.has("choices") || responseBody.get("choices").isEmpty()) {
                throw new NoMatchesFoundException("No matches found for the query.");
            }

            // Extract text response from OpenAI API's "choices" array
            String responseText = responseBody.get("choices").get(0).get("message").get("content").asText();

            // Return the response wrapped in a VectorSearchResponseDTO
            return List.of(new VectorSearchResponseDTO(provider, defaultModel, responseText, null));

        } catch (RestClientException e) {
            // Log error and throw a custom AIServiceException
            logger.error("Error while calling OpenAI Backend searchSimilarTexts: {}", e.getMessage(), e);
            throw new AIServiceException("OpenAI searchSimilarTexts failed: " + e.getMessage(), e);
        }
    }
}
