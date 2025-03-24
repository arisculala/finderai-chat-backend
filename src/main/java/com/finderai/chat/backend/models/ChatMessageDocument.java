package com.finderai.chat.backend.models;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.*;
import org.springframework.data.annotation.Id;
import org.springframework.data.elasticsearch.annotations.Document;
import org.springframework.data.elasticsearch.annotations.Field;
import org.springframework.data.elasticsearch.annotations.FieldType;

import java.time.LocalDateTime;
import java.util.Map;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Document(indexName = "chat_messages") // Elasticsearch index name
@Schema(description = "Chat message document stored in Elasticsearch")
public class ChatMessageDocument {

    @Id
    @Schema(description = "Unique identifier (matches PostgreSQL ID)", example = "1234567890123")
    private Long id;

    @Field(type = FieldType.Keyword)
    @Schema(description = "Bot ID related to the conversation", example = "bot_123")
    private String botId;

    @Field(type = FieldType.Keyword)
    @Schema(description = "User ID related to the conversation", example = "user_456")
    private String userId;

    @Field(type = FieldType.Text)
    @Schema(description = "Message content stored for search indexing", example = "Hello! How can I help you?")
    private String message;

    @Field(type = FieldType.Keyword)
    @Schema(description = "Sender of the message", example = "USER")
    private ChatMessage.Sender sender; // Use Enum for consistency

    @Field(type = FieldType.Date)
    @Schema(description = "Timestamp when the message was created", example = "2024-03-19T14:30:00")
    private LocalDateTime timestamp;

    @Field(type = FieldType.Object)
    @Schema(description = "Additional metadata stored as JSON")
    private Map<String, Object> metadata;
}
