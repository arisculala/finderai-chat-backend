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

import com.finderai.chat.backend.dto.VectorSearchRequestDTO;
import com.finderai.chat.backend.dto.VectorSearchResponseDTO;
import com.finderai.chat.backend.exceptions.AIServiceException;
import com.finderai.chat.backend.exceptions.NoMatchesFoundException;

@Service
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

    public List<VectorSearchResponseDTO> searchSimilarTexts(String query, String provider, String model, int limit) {
        logger.debug("Calling FinderAIService searchSimilarTexts method");

        String defaultModel = (model != null) ? model : apiModel;
        int defaultLimit = (limit == 0) ? limit : apiLimit;

        VectorSearchRequestDTO request = new VectorSearchRequestDTO(provider, defaultModel, query, defaultLimit);

        // prepare HTTP headers
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);

        // create HTTP entity
        HttpEntity<VectorSearchRequestDTO> entity = new HttpEntity<>(request, headers);

        try {
            // call FinderAI Backend
            ResponseEntity<VectorSearchResponseDTO[]> response = restTemplate.exchange(
                    apiUrl + "/api/v1/vectors/search",
                    HttpMethod.POST,
                    entity,
                    VectorSearchResponseDTO[].class);

            if (response.getBody() == null || response.getBody().length == 0) {
                throw new NoMatchesFoundException("No matches found for the query.");
            }

            // return list of results or empty list
            return response.getBody() != null ? List.of(response.getBody()) : Collections.emptyList();
        } catch (RestClientException e) {
            logger.error("Error while calling FinderAI Backend searchSimilarTexts: {}", e.getMessage(), e);
            throw new AIServiceException("FinderAI searchSimilarTexts failed: " + e.getMessage(), e);
        }
    }
}
