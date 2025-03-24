package com.finderai.chat.backend.services;

import java.util.Collections;
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

import com.finderai.chat.backend.dto.VectorSearchResponseDTO;
import com.finderai.chat.backend.exceptions.AIServiceException;
import com.finderai.chat.backend.exceptions.NoMatchesFoundException;

@Service
public class OpenAIService implements AIService {
    private static final Logger logger = LoggerFactory.getLogger(OpenAIService.class);

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

    @Override
    public List<VectorSearchResponseDTO> searchSimilarTexts(String query, String provider, String model, int limit) {
        logger.debug("Calling OpenAIService searchSimilarTexts method");

        String defaultModel = (model != null) ? model : apiModel;
        int defaultLimit = (limit == 0) ? limit : apiLimit;

        // OpenAI expects a JSON body with model, messages, etc.
        String requestBody = """
                    {
                        "model": "%s",
                        "messages": [{"role": "user", "content": "%s"}],
                        "max_tokens": 100
                    }
                """.formatted(model, query);

        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);
        headers.setBearerAuth(apiKey);

        HttpEntity<String> entity = new HttpEntity<>(requestBody, headers);

        try {
            ResponseEntity<VectorSearchResponseDTO[]> response = restTemplate.exchange(
                    apiUrl + "/v1/chat/completions",
                    HttpMethod.POST,
                    entity,
                    VectorSearchResponseDTO[].class);

            if (response.getBody() == null || response.getBody().length == 0) {
                throw new NoMatchesFoundException("No matches found for the query.");
            }

            return response.getBody() != null ? List.of(response.getBody()) : Collections.emptyList();
        } catch (RestClientException e) {
            logger.error("Error while calling OpenAI Backend searchSimilarTexts: {}", e.getMessage(), e);
            throw new AIServiceException("FinderAI searchSimilarTexts failed: " + e.getMessage(), e);
        }
    }
}
