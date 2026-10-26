package com.enterprise.knowledgeassistant.adapter.ai.agentChat;

import com.enterprise.knowledgeassistant.agent.tools.CandidateSearchTool;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class AiAgentChatClient implements AiAgentChat {

    private final org.springframework.ai.chat.client.ChatClient.Builder chatClientBuilder;
    private final CandidateSearchTool candidateSearchTool;

    public String generateAnswer(String query) {

        return chatClientBuilder.build()
                .prompt()
                .user(query)
                .tools(candidateSearchTool)
                .call()
                .content();
    }
}