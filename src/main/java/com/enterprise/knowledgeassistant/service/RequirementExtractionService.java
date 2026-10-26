package com.enterprise.knowledgeassistant.service;

import com.enterprise.knowledgeassistant.dto.CandidateRequirements;

public interface RequirementExtractionService {

    CandidateRequirements extract(String recruiterRequest);
}