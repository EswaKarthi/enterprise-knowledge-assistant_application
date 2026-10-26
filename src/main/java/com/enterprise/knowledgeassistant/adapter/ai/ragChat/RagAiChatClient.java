package com.enterprise.knowledgeassistant.adapter.ai.ragChat;

import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

@Component
@Slf4j
public class RagAiChatClient implements ChatClient {

    private final org.springframework.ai.chat.client.ChatClient chatClient;

    public RagAiChatClient(
            org.springframework.ai.chat.client.ChatClient.Builder chatClientBuilder) {

        this.chatClient = chatClientBuilder.build();
    }

    @Override
    public String generateAnswer(String prompt) {

        long startTime = System.nanoTime();

        log.info(
                "Starting Gemini LLM call. Prompt length: {} characters",
                prompt == null ? 0 : prompt.length()
        );

        try {
            String answer = chatClient
                    .prompt()
                    .user(prompt)
                    .call()
                    .content();

            long elapsedMs =
                    (System.nanoTime() - startTime) / 1_000_000;

            log.info(
                    "Gemini LLM call completed successfully in {} ms",
                    elapsedMs
            );

            return answer;

        } catch (Exception ex) {

            long elapsedMs =
                    (System.nanoTime() - startTime) / 1_000_000;

            log.error(
                    "Gemini LLM call failed after {} ms",
                    elapsedMs,
                    ex
            );

            throw ex;
        }
    }
}