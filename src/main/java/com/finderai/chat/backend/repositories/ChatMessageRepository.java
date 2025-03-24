package com.finderai.chat.backend.repositories;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.finderai.chat.backend.models.ChatMessage;

@Repository
public interface ChatMessageRepository extends JpaRepository<ChatMessage, Long> {

    // Find messages by botId and userId, ordered by timestamp (with pagination)
    Page<ChatMessage> findByBotIdAndUserIdOrderByTimestampAsc(String botId, String userId, Pageable pageable);

    // Find messages by userId, ordered by timestamp (with pagination)
    Page<ChatMessage> findByUserIdOrderByTimestampAsc(String userId, Pageable pageable);
}
