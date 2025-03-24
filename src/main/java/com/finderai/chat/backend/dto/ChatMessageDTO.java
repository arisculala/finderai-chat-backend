package com.finderai.chat.backend.dto;

import java.time.LocalDateTime;
import java.util.Map;

import com.finderai.chat.backend.models.ChatMessage;
import com.finderai.chat.backend.models.ChatMessage.Sender;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class ChatMessageDTO {

    private Long id;

    private String botId;

    private String userId;

    private String message;

    private Sender sender;

    private LocalDateTime timestamp;

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