package com.enterprise.knowledgeassistant.service;

import com.enterprise.knowledgeassistant.dto.request.ChatRequest;
import com.enterprise.knowledgeassistant.dto.response.ChatResponse;

public interface ChatService {
    ChatResponse chat(ChatRequest request);
}
