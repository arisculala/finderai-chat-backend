package com.finderai.chat.backend.dto;

import java.time.LocalDateTime;
import java.util.Map;

import com.finderai.chat.backend.models.ChatMessage;
import com.finderai.chat.backend.models.ChatMessage.Sender;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Schema(description = "Represents a chat message")
public class ChatMessageDTO {

    @Schema(description = "Unique ID of the chat message", example = "1")
    private Long id;

    @Schema(description = "Bot ID involved in the chat", example = "bot123")
    private String botId;

    @Schema(description = "User ID of the sender", example = "user456")
    private String userId;

    @Schema(description = "Content of the message", example = "Hello, how can I help you?")
    private String message;

    @Schema(description = "Sender of the message (USER or BOT)", example = "USER")
    private Sender sender;

    @Schema(description = "Timestamp when the message was sent", example = "2025-03-19T14:30:00")
    private LocalDateTime timestamp;

    @Schema(description = "Additional metadata related to the message")
    private Map<String, Object> metadata;

    public ChatMessageDTO(ChatMessage chatMessage) {
        this.id = chatMessage.getId();
        this.botId = chatMessage.getBotId();
        this.userId = chatMessage.getUserId();
        this.message = chatMessage.getMessage();
        this.sender = chatMessage.getSender();
        this.timestamp = chatMessage.getTimestamp();
    }
}
