package com.enterprise.knowledgeassistant.controller;

import com.enterprise.knowledgeassistant.dto.DocumentUploadResponse;
import com.enterprise.knowledgeassistant.service.DocumentService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

@RestController
@RequestMapping("/api/v1/documents")
@RequiredArgsConstructor
public class DocumentController {

    private final DocumentService documentService;

    @PostMapping(
            value = "/upload",
            consumes = MediaType.MULTIPART_FORM_DATA_VALUE
    )
    public ResponseEntity<DocumentUploadResponse> uploadDocument(
            @RequestParam("file") MultipartFile file) {

        DocumentUploadResponse response =
                documentService.uploadDocument(file);

        return ResponseEntity.ok(response);
    }
}