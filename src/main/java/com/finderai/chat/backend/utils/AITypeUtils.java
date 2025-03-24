package com.finderai.chat.backend.utils;

import com.finderai.chat.backend.enums.AIType;

import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.Stream;

public class AITypeUtils {

    /**
     * Returns a list of all available AI types as strings.
     */
    public static List<String> getAllAITypes() {
        return Stream.of(AIType.values())
                .map(AIType::getType)
                .collect(Collectors.toList());
    }

    /**
     * Validates if a given AI type exists.
     */
    public static boolean isValidAIType(String type) {
        return getAllAITypes().contains(type.toLowerCase());
    }
}
