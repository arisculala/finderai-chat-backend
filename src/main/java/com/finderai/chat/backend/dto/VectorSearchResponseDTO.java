package com.finderai.chat.backend.dto;

import java.util.Map;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Schema(description = "Response payload for vector search results")
public class VectorSearchResponseDTO {

    @Schema(description = "AI provider that generated the response", example = "finderai")
    private String provider;

    @Schema(description = "AI model used", example = "gpt-4")
    private String model;

    @Schema(description = "Generated response text", example = "AI is a field of computer science that enables machines to learn and make decisions.")
    private String text;

    @Schema(description = "Additional metadata associated with the response")
    private Map<String, Object> metadata;
}
