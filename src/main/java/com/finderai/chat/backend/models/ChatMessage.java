package com.finderai.chat.backend.models;

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
public class ChatMessage {

    @Id
    private Long id;

    @Column(nullable = false)
    private String botId;

    @Column(nullable = false)
    private String userId;

    @Column(nullable = false, columnDefinition = "TEXT")
    private String message;

    @Enumerated(EnumType.STRING)
    private Sender sender;

    @Column(nullable = false, updatable = false)
    private LocalDateTime timestamp;

    @JdbcTypeCode(SqlTypes.JSON)
    @Column(columnDefinition = "jsonb", nullable = true)
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
