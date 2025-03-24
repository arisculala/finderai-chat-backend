package com.finderai.chat.backend.dto;

import com.finderai.chat.backend.models.ChatMessage;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
@AllArgsConstructor
@Schema(description = "Paginated response for chat history.")
public class ChatHistoryResponseDTO {

    @Schema(description = "List of chat messages.", example = "[{id: 1, botId: 'bot123', userId: 'user456', message: 'Hello!', sender: 'USER', timestamp: '2024-03-19T10:15:30'}]")
    private List<ChatMessage> messages;

    @Schema(description = "Current page number.", example = "0")
    private int page;

    @Schema(description = "Number of messages per page.", example = "20")
    private int size;

    @Schema(description = "Total number of pages.", example = "5")
    private int totalPages;

    @Schema(description = "Total number of messages available.", example = "100")
    private long totalMessages;
}
