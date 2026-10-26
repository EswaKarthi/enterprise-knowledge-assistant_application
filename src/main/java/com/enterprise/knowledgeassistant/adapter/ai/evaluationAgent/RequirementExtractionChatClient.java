package com.enterprise.knowledgeassistant.adapter.ai.evaluationAgent;

import com.enterprise.knowledgeassistant.dto.CandidateRequirements;

public interface RequirementExtractionChatClient {

    CandidateRequirements extractRequirements(String recruiterRequest);
}