package com.enterprise.knowledgeassistant.agent.workflow;

import com.enterprise.knowledgeassistant.agent.tools.CandidateSearchTool;
import lombok.extern.slf4j.Slf4j;
import org.springframework.ai.chat.client.ChatClient;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
@Slf4j
public class AgentWorkflow {

    private static final int MAX_ADDITIONAL_SEARCHES = 2;

    private final ChatClient chatClient;
    private final CandidateSearchTool candidateSearchTool;

    public AgentWorkflow(
            ChatClient.Builder chatClientBuilder,
            CandidateSearchTool candidateSearchTool) {

        this.chatClient = chatClientBuilder.build();
        this.candidateSearchTool = candidateSearchTool;
    }

    public String execute(String userQuery) {

        if (userQuery == null || userQuery.isBlank()) {
            throw new IllegalArgumentException(
                    "Agent query must not be empty"
            );
        }

        List<String> collectedEvidence = new ArrayList<>();

        String currentQuery = userQuery;
        int additionalSearches = 0;

        while (true) {

            log.info(
                    "Executing candidate search. Additional searches: {}",
                    additionalSearches
            );

            List<String> searchResults =
                    candidateSearchTool.searchCandidates(currentQuery);

            if (searchResults != null) {
                collectedEvidence.addAll(searchResults);
            }

            boolean sufficient =
                    evaluateSufficiency(userQuery, collectedEvidence);

            if (sufficient) {
                log.info("Sufficient evidence found");
                break;
            }

            if (additionalSearches >= MAX_ADDITIONAL_SEARCHES) {
                log.info("Maximum additional searches reached");
                break;
            }

            String nextQuery =
                    generateNextSearchQuery(
                            userQuery,
                            collectedEvidence
                    );

            if (nextQuery == null
                    || nextQuery.isBlank()
                    || nextQuery.equalsIgnoreCase(currentQuery)) {

                log.info("No useful alternative search query generated");
                break;
            }

            currentQuery = nextQuery;
            additionalSearches++;
        }

        return generateFinalAnswer(userQuery, collectedEvidence);
    }

    private boolean evaluateSufficiency(
            String userQuery,
            List<String> evidence) {

        String prompt = """
                You are evaluating evidence for a candidate search assistant.

                Original recruiter request:
                %s

                Retrieved evidence:
                %s

                Determine whether the retrieved evidence contains
                enough relevant information to answer the original
                request accurately.

                Do not assume missing information is true.

                Respond with exactly one word:
                SUFFICIENT
                or
                INSUFFICIENT
                """.formatted(userQuery, evidence);

        String result = chatClient
                .prompt()
                .user(prompt)
                .call()
                .content();

        return result != null
                && result.trim().equalsIgnoreCase("SUFFICIENT");
    }

    private String generateNextSearchQuery(
            String userQuery,
            List<String> evidence) {

        String prompt = """
                You are helping a candidate search workflow retrieve
                more relevant resume evidence.

                Original recruiter request:
                %s

                Evidence already retrieved:
                %s

                Suggest ONE alternative search query that could retrieve
                useful additional evidence.

                Do not answer the recruiter.
                Do not invent candidate details.
                Return only the search query.

                If no useful alternative search is possible,
                return NO_USEFUL_QUERY.
                """.formatted(userQuery, evidence);

        String nextQuery = chatClient
                .prompt()
                .user(prompt)
                .call()
                .content();

        if (nextQuery == null
                || nextQuery.isBlank()
                || nextQuery.trim().equalsIgnoreCase("NO_USEFUL_QUERY")) {
            return null;
        }

        return nextQuery.trim();
    }

    private String generateFinalAnswer(
            String userQuery,
            List<String> evidence) {

        String prompt = """
                You are an evidence-based candidate search assistant.

                Recruiter request:
                %s

                Retrieved resume evidence:
                %s

                Answer the recruiter using only the supplied evidence.

                Rules:
                1. Do not invent candidate names, skills, or experience.
                2. Distinguish confirmed facts from missing information.
                3. Do not claim a candidate meets a requirement unless
                   the evidence supports that claim.
                4. If the evidence is insufficient, explain the limitation.
                5. If no relevant evidence was found, say so clearly.
                """.formatted(userQuery, evidence);

        String answer = chatClient
                .prompt()
                .user(prompt)
                .call()
                .content();

        return answer == null || answer.isBlank()
                ? "I could not generate a reliable answer from the available evidence."
                : answer;
    }
}