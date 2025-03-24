package com.finderai.chat.backend.enums;

import io.swagger.v3.oas.annotations.media.Schema;

@Schema(description = "Enumeration for supported AI providers")
public enum AIType {
    @Schema(description = "FinderAI provider", example = "finderai")
    FINDER_AI("finderai"),

    @Schema(description = "OpenAI provider", example = "openai")
    OPEN_AI("openai"),

    @Schema(description = "DeepSeek AI provider", example = "deepseek")
    DEEPSEEK("deepseek"),

    @Schema(description = "Hugging Face AI provider", example = "huggingface")
    HUGGINGFACE("huggingface");

    private final String type;

    AIType(String type) {
        this.type = type;
    }

    public String getType() {
        return type;
    }

    @Override
    public String toString() {
        return type;
    }

    public static AIType fromString(String type) {
        for (AIType aiType : AIType.values()) {
            if (aiType.type.equalsIgnoreCase(type)) {
                return aiType;
            }
        }
        throw new IllegalArgumentException("Unknown AI type: " + type);
    }
}
