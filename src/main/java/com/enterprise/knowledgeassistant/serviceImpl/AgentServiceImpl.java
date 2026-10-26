package com.enterprise.knowledgeassistant.serviceImpl;

import com.enterprise.knowledgeassistant.adapter.ai.agentChat.AiAgentChat;
import com.enterprise.knowledgeassistant.service.AgentService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class AgentServiceImpl implements AgentService {

    private final AiAgentChat agentClient;

    @Override
    public String execute(String message) {

        if (message == null || message.isBlank()) {
            throw new IllegalArgumentException(
                    "Agent message must not be empty"
            );
        }

        return agentClient.generateAnswer(message);
    }
}
