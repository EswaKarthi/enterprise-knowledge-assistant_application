package com.enterprise.knowledgeassistant.service;

import com.enterprise.knowledgeassistant.dto.DocumentUploadResponse;
import org.springframework.web.multipart.MultipartFile;

public interface DocumentService {

    DocumentUploadResponse uploadDocument(MultipartFile file);
}
