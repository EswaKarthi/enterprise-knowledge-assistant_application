package com.enterprise.knowledgeassistant.dto;

import java.util.List;

public record CandidateRequirements(
        List<Requirement> mandatoryRequirements,
        List<Requirement> preferredRequirements
) {
}