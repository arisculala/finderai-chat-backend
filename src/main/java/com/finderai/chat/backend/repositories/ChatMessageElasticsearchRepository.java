package com.finderai.chat.backend.repositories;

import org.springframework.data.elasticsearch.repository.ElasticsearchRepository;
import org.springframework.stereotype.Repository;

import com.finderai.chat.backend.models.ChatMessageDocument;

@Repository
public interface ChatMessageElasticsearchRepository extends ElasticsearchRepository<ChatMessageDocument, Long> {
}
