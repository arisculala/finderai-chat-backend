package com.finderai.chat.backend.repositories;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import com.finderai.chat.backend.models.ChatMessage;

@Repository
public interface ChatMessageRepository extends JpaRepository<ChatMessage, Long> {

    // Find messages by botId and userId, ordered by timestamp (with pagination)
    Page<ChatMessage> findByBotIdAndUserIdOrderByTimestampAsc(String botId, String userId, Pageable pageable);

    @Query("""
                SELECT cm FROM ChatMessage cm
                WHERE cm.userId = :userId
                AND cm.botId = :botId
                AND (:sender IS NULL OR :sender = '' OR cm.sender = :sender)
                ORDER BY cm.timestamp ASC
            """)
    Page<ChatMessage> findByBotIdAndUserIdAndOptionalSender(
            @Param("botId") String botId,
            @Param("userId") String userId,
            @Param("sender") ChatMessage.Sender sender, // Use enum type
            Pageable pageable);

    // Find messages by userId, ordered by timestamp (with pagination)
    Page<ChatMessage> findByUserIdOrderByTimestampAsc(String userId, Pageable pageable);

    @Query("""
                SELECT cm FROM ChatMessage cm
                WHERE cm.userId = :userId
                AND (:sender IS NULL OR :sender = '' OR cm.sender = :sender)
                ORDER BY cm.timestamp ASC
            """)
    Page<ChatMessage> findBydUserIdAndOptionalSender(
            @Param("userId") String userId,
            @Param("sender") ChatMessage.Sender sender, // Use enum type
            Pageable pageable);
}
