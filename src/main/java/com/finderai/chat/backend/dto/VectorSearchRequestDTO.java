package com.finderai.chat.backend.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class VectorSearchRequestDTO {
    private String provider;
    private String model;
    private String query;
    private int limit = 5; // Default limit if not provided
}
