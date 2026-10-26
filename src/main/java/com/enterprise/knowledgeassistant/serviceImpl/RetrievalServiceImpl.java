package com.enterprise.knowledgeassistant.serviceImpl;

import com.enterprise.knowledgeassistant.adapter.ai.vector.PgVectorStoreAdapter;
import com.enterprise.knowledgeassistant.service.RetrievalService;
import lombok.RequiredArgsConstructor;
import org.springframework.ai.document.Document;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class RetrievalServiceImpl implements RetrievalService {

    private final PgVectorStoreAdapter pgVectorStoreAdapter;

    @Override
    public List<Document> retrieveRelevantChunks(String query) {

        if (query == null || query.isBlank()) {
            throw new IllegalArgumentException(
                    "Search query must not be empty");
        }

        return pgVectorStoreAdapter.similaritySearch(query, 5);
    }
}