package com.enterprise.knowledgeassistant.serviceImpl;

import com.enterprise.knowledgeassistant.dto.request.ChatRequest;
import com.enterprise.knowledgeassistant.dto.response.ChatResponse;
import com.enterprise.knowledgeassistant.service.ChatService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class ChatServiceImpl implements ChatService {

    @Override
    public ChatResponse chat(ChatRequest request) {

        // RAG workflow will be implemented here later.

        throw new UnsupportedOperationException(
                "Chat workflow is not implemented yet"
        );
    }
}
