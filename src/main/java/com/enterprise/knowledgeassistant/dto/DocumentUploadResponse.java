package com.enterprise.knowledgeassistant.dto;

public record DocumentUploadResponse(
        String status,
        String message,
        String fileName,
        long fileSizeBytes,
        String fileType
) {
}
