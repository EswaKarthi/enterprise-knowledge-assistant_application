package com.enterprise.knowledgeassistant.service;

import org.springframework.ai.document.Document;

import java.util.List;

public interface RetrievalService {

    List<Document> retrieveRelevantChunks(String query);
}