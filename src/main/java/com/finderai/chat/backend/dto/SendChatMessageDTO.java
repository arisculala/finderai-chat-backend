package com.finderai.chat.backend.dto;

import java.util.Map;

import com.finderai.chat.backend.enums.AIType;
import com.finderai.chat.backend.models.ChatMessage.Sender;

import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class SendChatMessageDTO {

    @NotBlank
    private Sender sender;

    private String botId;

    @NotBlank
    private String userId;

    @NotBlank
    private String message;

    private Map<String, Object> metadata;

    @Min(1)
    private int limit = 5; // Default to 5 if not provided

    private AIType aiType;
}