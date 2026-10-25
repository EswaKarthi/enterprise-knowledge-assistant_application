package com.enterprise.knowledgeassistant.adapter.document;

import org.springframework.ai.document.Document;
import org.springframework.ai.transformer.splitter.TokenTextSplitter;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
public class DocumentChunker {

    private final TokenTextSplitter textSplitter;

    public DocumentChunker() {
        this.textSplitter = TokenTextSplitter.builder()
                .withChunkSize(500)
                .withMinChunkSizeChars(100)
                .withMinChunkLengthToEmbed(10)
                .withMaxNumChunks(10000)
                .build();
    }

    public List<String> chunkText(String text) {

        if (text == null || text.isBlank()) {
            return List.of();
        }

        Document document = new Document(text);

        return textSplitter.split(document)
                .stream()
                .map(Document::getText)
                .filter(chunk -> chunk != null && !chunk.isBlank())
                .toList();
    }
}