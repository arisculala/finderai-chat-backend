package com.finderai.chat.backend.dto;

import java.util.Map;

import com.finderai.chat.backend.enums.AIType;
import com.finderai.chat.backend.models.ChatMessage.Sender;

import io.swagger.v3.oas.annotations.media.Schema;
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
@Schema(description = "Request payload for sending a chat message")
public class SendChatMessageDTO {

    @NotBlank
    @Schema(description = "Sender of the message (USER or BOT)", example = "USER")
    private Sender sender;

    @Schema(description = "Bot ID if applicable", example = "bot123")
    private String botId;

    @NotBlank
    @Schema(description = "User ID of the sender", example = "user456")
    private String userId;

    @NotBlank
    @Schema(description = "Content of the message", example = "What's the weather today?")
    private String message;

    @Schema(description = "Optional metadata for the message")
    private Map<String, Object> metadata;

    @Min(1)
    @Schema(description = "Number of results to return", example = "5", defaultValue = "5")
    private int limit = 5;

    @Schema(description = "AI provider type", example = "FINDER_AI")
    private AIType aiType;
}
