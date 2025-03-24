package com.finderai.chat.backend.enums;

public enum AIType {
    FINDER_AI("finderai"),
    OPEN_AI("openai"),
    DEEPSEEK("deepseek"),
    HUGGINGFACE("huggingface");

    private final String type;

    AIType(String type) {
        this.type = type;
    }

    public String getType() {
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
