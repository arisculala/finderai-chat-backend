package com.finderai.chat.backend.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Schema(description = "Request payload for vector-based text search")
public class VectorSearchRequestDTO {

    @Schema(description = "AI provider for the search", example = "finderai")
    private String provider;

    @Schema(description = "AI model name used for the search", example = "gpt-4")
    private String model;

    @Schema(description = "Search query text", example = "How does AI work?")
    private String query;

    @Schema(description = "Number of results to return", example = "5", defaultValue = "5")
    private int limit = 5;
}
