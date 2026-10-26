package com.enterprise.knowledgeassistant.adapter.ai.agentChat;

import com.enterprise.knowledgeassistant.agent.tools.CandidateSearchTool;
import lombok.extern.slf4j.Slf4j;
import org.springframework.ai.chat.client.ChatClient;
import org.springframework.stereotype.Component;

@Component
@Slf4j
public class AiAgentChatClient implements AiAgentChat {

    private final ChatClient chatClient;
    private final CandidateSearchTool candidateSearchTool;

    public AiAgentChatClient(
            ChatClient.Builder chatClientBuilder,
            CandidateSearchTool candidateSearchTool) {

        this.chatClient = chatClientBuilder.build();
        this.candidateSearchTool = candidateSearchTool;
    }

    @Override
    public String generateAnswer(String query) {

        long startTime = System.nanoTime();

        log.info(
                "Starting Gemini LLM agent call. Query length: {} characters",
                query == null ? 0 : query.length()
        );

        try {
            String answer = chatClient
                    .prompt()
                    .user(query)
                    .tools(candidateSearchTool)
                    .call()
                    .content();

            long elapsedMs =
                    (System.nanoTime() - startTime) / 1_000_000;

            log.info(
                    "Gemini LLM agent call completed successfully in {} ms",
                    elapsedMs
            );

            return answer;

        } catch (Exception ex) {

            long elapsedMs =
                    (System.nanoTime() - startTime) / 1_000_000;

            log.error(
                    "Gemini LLM agent call failed after {} ms",
                    elapsedMs,
                    ex
            );

            throw ex;
        }
    }
}