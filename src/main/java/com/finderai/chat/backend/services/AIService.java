package com.finderai.chat.backend.services;

import com.finderai.chat.backend.dto.VectorSearchResponseDTO;

import java.util.List;

public interface AIService {
    /**
     * Searches for the most relevant text response based on the query.
     *
     * @param query    The input text from the user.
     * @param provider The provider name.
     * @param model    The model name.
     * @param limit    The maximum number of results.
     * @return A list of matching responses.
     */
    List<VectorSearchResponseDTO> searchSimilarTexts(String query, String provider, String model, int limit);
}
