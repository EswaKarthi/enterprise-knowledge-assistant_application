package com.enterprise.knowledgeassistant.serviceImpl;

import com.enterprise.knowledgeassistant.adapter.ai.evaluationAgent.RequirementExtractionChatClient;
import com.enterprise.knowledgeassistant.dto.CandidateRequirements;
import com.enterprise.knowledgeassistant.service.RequirementExtractionService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class RequirementExtractionServiceImpl
        implements RequirementExtractionService {

    private final RequirementExtractionChatClient chatClient;

    @Override
    public CandidateRequirements extract(String recruiterRequest) {

        if (recruiterRequest == null || recruiterRequest.isBlank()) {
            throw new IllegalArgumentException(
                    "Recruiter request must not be empty"
            );
        }

        return chatClient.extractRequirements(recruiterRequest);
    }
}