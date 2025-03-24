package com.finderai.chat.backend.dto;

import java.util.Map;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class VectorSearchResponseDTO {
    private String provider;

    private String model;

    private String text;

    private Map<String, Object> metadata;

    // @SuppressWarnings("unchecked")
    // public VectorSearchResponseDTO(Map<String, Object> response) {
    // this.provider = (String) response.get("provider");
    // this.model = (String) response.get("provider");
    // this.text = (String) response.get("provider");
    // this.metadata = (Map<String, Object>) response.get("metadata");
    // }
}
