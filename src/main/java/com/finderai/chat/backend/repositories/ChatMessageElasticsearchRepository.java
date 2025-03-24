package com.finderai.chat.backend.repositories;

import org.springframework.data.elasticsearch.annotations.Query;
import org.springframework.data.elasticsearch.repository.ElasticsearchRepository;
import org.springframework.stereotype.Repository;
import com.finderai.chat.backend.models.ChatMessageDocument;
import java.util.List;

@Repository
public interface ChatMessageElasticsearchRepository extends ElasticsearchRepository<ChatMessageDocument, Long> {

    // Find chat messages by botId and userId, sorted by timestamp
    List<ChatMessageDocument> findByBotIdAndUserIdOrderByTimestampAsc(String botId, String userId);

    // Find messages for a user, sorted by timestamp
    List<ChatMessageDocument> findByUserIdOrderByTimestampAsc(String userId);

    // Search for messages containing a keyword (Elasticsearch DSL query)
    @Query("{\"match\": {\"message\": \"?0\"}}")
    List<ChatMessageDocument> searchByMessageContent(String keyword);
}
