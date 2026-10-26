package com.enterprise.knowledgeassistant.adapter.ai.evaluationAgent;

import com.enterprise.knowledgeassistant.dto.CandidateRequirements;
import lombok.extern.slf4j.Slf4j;
import org.springframework.ai.chat.client.ChatClient;
import org.springframework.stereotype.Component;

@Component
@Slf4j
public class RequirementExtractionChatClientImpl
        implements RequirementExtractionChatClient {

    private final ChatClient chatClient;

    public RequirementExtractionChatClientImpl(
            ChatClient.Builder chatClientBuilder) {

        this.chatClient = chatClientBuilder.build();
    }

    @Override
    public CandidateRequirements extractRequirements(
            String recruiterRequest) {

        log.info("Starting requirement extraction LLM call");

        String systemPrompt = """
                You extract structured candidate requirements
                from recruiter requests.

                Identify mandatory and preferred requirements.

                Rules:
                1. Do not invent requirements.
                2. Preserve explicitly stated minimum experience.
                3. Treat a requirement as mandatory only when
                   the request makes it mandatory or clearly
                   requires it.
                4. Treat explicitly preferred skills as preferred.
                5. Do not convert preferred requirements into
                   mandatory requirements.
                6. Preserve ambiguity when a requirement is unclear.
                7. Return structured output matching the requested
                   CandidateRequirements type.
                """;

        try {
            CandidateRequirements requirements = chatClient
                    .prompt()
                    .system(systemPrompt)
                    .user(recruiterRequest)
                    .call()
                    .entity(CandidateRequirements.class);

            log.info("Requirement extraction completed successfully");

            return requirements;

        } catch (Exception ex) {
            log.error("Requirement extraction LLM call failed", ex);
            throw ex;
        }
    }
}