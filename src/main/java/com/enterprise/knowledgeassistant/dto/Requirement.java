package com.enterprise.knowledgeassistant.dto;

public record Requirement(
        String skill,
        String description,
        Integer minimumYears,
        boolean mandatory
) {
}