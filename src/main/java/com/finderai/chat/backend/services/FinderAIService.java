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

import com.finderai.chat.backend.dto.VectorSearchRequestDTO;
import com.finderai.chat.backend.dto.VectorSearchResponseDTO;
import com.finderai.chat.backend.exceptions.AIServiceException;
import com.finderai.chat.backend.exceptions.NoMatchesFoundException;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;

/**
 * Service responsible for searching similar text responses using FinderAI.
 */
@Service
@Tag(name = "FinderAI Service", description = "Handles requests to FinderAI for searching similar text responses.")
public class FinderAIService implements AIService {
    private static final Logger logger = LoggerFactory.getLogger(FinderAIService.class);

    @Value("${ai.provider.finderai.api.url}")
    private String apiUrl;

    @Value("${ai.provider.finderai.api.key}")
    private String apiKey;

    @Value("${ai.provider.finderai.api.model}")
    private String apiModel;

    @Value("${ai.provider.finderai.api.limit}")
    private int apiLimit;

    private final RestTemplate restTemplate;

    public FinderAIService(RestTemplate restTemplate) {
        this.restTemplate = restTemplate;
    }

    /**
     * Searches for similar text responses using FinderAI.
     *
     * @param query    The input text to search for.
     * @param provider The AI provider name.
     * @param model    The AI model to use.
     * @param limit    The number of results to return.
     * @return A list of similar text responses.
     */
    @Operation(summary = "Search similar texts using FinderAI", description = "Finds similar text responses using FinderAI's AI model.")
    public List<VectorSearchResponseDTO> searchSimilarTexts(String query, String provider, String model, int limit) {
        logger.debug("Calling FinderAIService searchSimilarTexts method");

        String defaultModel = (model != null) ? model : apiModel;
        int defaultLimit = (limit == 0) ? apiLimit : limit;

        VectorSearchRequestDTO request = new VectorSearchRequestDTO(provider, defaultModel, query, defaultLimit);

        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);

        HttpEntity<VectorSearchRequestDTO> entity = new HttpEntity<>(request, headers);

        try {
            ResponseEntity<VectorSearchResponseDTO[]> response = restTemplate.exchange(
                    apiUrl + "/api/v1/vectors/search",
                    HttpMethod.POST,
                    entity,
                    VectorSearchResponseDTO[].class);

            if (response.getBody() == null || response.getBody().length == 0) {
                throw new NoMatchesFoundException("No matches found for the query.");
            }

            return List.of(response.getBody());
        } catch (RestClientException e) {
            logger.error("Error while calling FinderAI searchSimilarTexts: {}", e.getMessage(), e);
            throw new AIServiceException("FinderAI searchSimilarTexts failed: " + e.getMessage(), e);
        }
    }
}
