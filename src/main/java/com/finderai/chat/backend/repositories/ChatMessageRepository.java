package com.finderai.chat.backend.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import com.finderai.chat.backend.models.ChatMessage;
import java.util.List;

public interface ChatMessageRepository extends JpaRepository<ChatMessage, Long> {

    // Find messages by userId and botId, ordered by timestamp (ascending)
    List<ChatMessage> findByBotIdAndUserIdOrderByTimestampAsc(String botId, String userId);

    // Find messages by userId, ordered by timestamp (ascending)
    List<ChatMessage> findByUserIdOrderByTimestampAsc(String userId);
}
