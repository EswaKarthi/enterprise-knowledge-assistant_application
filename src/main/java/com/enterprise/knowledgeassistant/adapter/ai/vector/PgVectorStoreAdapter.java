package com.enterprise.knowledgeassistant.adapter.ai.vector;

import lombok.RequiredArgsConstructor;
import org.springframework.ai.document.Document;
import org.springframework.ai.vectorstore.SearchRequest;
import org.springframework.ai.vectorstore.VectorStore;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.UUID;

@Component
@RequiredArgsConstructor
public class PgVectorStoreAdapter {

    private final VectorStore vectorStore;

    public void storeChunks(List<String> chunks, String fileName) {

        if (chunks == null || chunks.isEmpty()) {
            return;
        }

        List<Document> documents = chunks.stream()
                .map(chunk -> Document.builder()
                        .id(UUID.randomUUID().toString())
                        .text(chunk)
                        .metadata("fileName", fileName)
                        .build())
                .toList();

        vectorStore.add(documents);
    }


    public List<Document> similaritySearch(String query, int topK) {

        SearchRequest searchRequest = SearchRequest.builder()
                .query(query)
                .topK(topK)
                .build();

        return vectorStore.similaritySearch(searchRequest);
    }

}