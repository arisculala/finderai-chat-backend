package com.finderai.chat.backend.models;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDateTime;
import java.util.Map;

import org.hibernate.annotations.JdbcTypeCode;
import org.hibernate.type.SqlTypes;

import com.generic.utils.SnowflakeIdGenerator;

@Entity
@Table(name = "chat_messages")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Schema(description = "Chat message entity stored in PostgreSQL")
public class ChatMessage {

    @Id
    @Schema(description = "Unique identifier (generated via Snowflake ID)", example = "1234567890123")
    private Long id;

    @Column(nullable = false)
    @Schema(description = "Bot ID related to the conversation", example = "bot_123")
    private String botId;

    @Column(nullable = false)
    @Schema(description = "User ID sending or receiving the message", example = "user_456")
    private String userId;

    @Column(nullable = false, columnDefinition = "TEXT")
    @Schema(description = "Actual chat message content", example = "Hello! How can I help you?")
    private String message;

    @Enumerated(EnumType.STRING)
    @Schema(description = "Sender of the message (USER or BOT)", example = "USER")
    private Sender sender;

    @Column(nullable = false, updatable = false)
    @Schema(description = "Timestamp when the message was created", example = "2024-03-19T14:30:00")
    private LocalDateTime timestamp;

    @JdbcTypeCode(SqlTypes.JSON)
    @Column(columnDefinition = "jsonb", nullable = true)
    @Schema(description = "Additional metadata stored as JSON")
    private Map<String, Object> metadata;

    @PrePersist
    public void prePersist() {
        if (this.id == null) {
            this.id = SnowflakeIdGenerator.getInstance(1).nextId();
        }
        this.timestamp = LocalDateTime.now();
    }

    public enum Sender {
        USER, BOT
    }
}
