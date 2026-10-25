package com.enterprise.knowledgeassistant.serviceImpl;

import com.enterprise.knowledgeassistant.adapter.ai.vector.PgVectorStoreAdapter;
import com.enterprise.knowledgeassistant.adapter.document.DocumentChunker;
import com.enterprise.knowledgeassistant.adapter.document.DocumentTextExtractor;
import com.enterprise.knowledgeassistant.dto.DocumentUploadResponse;
import com.enterprise.knowledgeassistant.service.DocumentService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.server.ResponseStatusException;

import java.util.List;
import java.util.Locale;
import java.util.Set;

@Service
@RequiredArgsConstructor
@Slf4j
public class DocumentServiceImpl implements DocumentService {

    private final DocumentTextExtractor documentTextExtractor;
    private final DocumentChunker documentChunker;
    private final PgVectorStoreAdapter pgVectorStoreAdapter;

    private static final long MAX_FILE_SIZE = 10 * 1024 * 1024;

    private static final Set<String> ALLOWED_EXTENSIONS =
            Set.of("pdf", "docx", "txt");

    @Override
    public DocumentUploadResponse uploadDocument(MultipartFile file) {

        validateFile(file);

        String fileName = file.getOriginalFilename();
        String extension = getExtension(fileName);

        String extractedText = documentTextExtractor.extractText(file);
        List<String> chunks = documentChunker.chunkText(extractedText);

        log.info(
                "Document '{}' extracted successfully. Generated {} chunks.",
                file.getOriginalFilename(),
                chunks.size()
        );

        if (!chunks.isEmpty()) {
            pgVectorStoreAdapter.storeChunks(chunks, fileName);

            log.info(
                    "Document '{}' stored successfully. Total chunks: {}",
                    fileName,
                    chunks.size()
            );
        }

        return new DocumentUploadResponse(
                "SUCCESS",
                "File accepted for processing",
                fileName,
                file.getSize(),
                extension
        );
    }

    private void validateFile(MultipartFile file) {

        if (file == null || file.isEmpty()) {
            throw new ResponseStatusException(
                    HttpStatus.BAD_REQUEST,
                    "Please upload a non-empty file"
            );
        }

        if (file.getSize() > MAX_FILE_SIZE) {
            throw new ResponseStatusException(
                    HttpStatus.PAYLOAD_TOO_LARGE,
                    "File size must not exceed 10 MB"
            );
        }

        String fileName = file.getOriginalFilename();

        if (fileName == null || fileName.isBlank()) {
            throw new ResponseStatusException(
                    HttpStatus.BAD_REQUEST,
                    "File name is required"
            );
        }

        String extension = getExtension(fileName);

        if (!ALLOWED_EXTENSIONS.contains(extension)) {
            throw new ResponseStatusException(
                    HttpStatus.BAD_REQUEST,
                    "Unsupported file type. Allowed: PDF, DOCX, TXT"
            );
        }
    }

    private String getExtension(String fileName) {

        int lastDot = fileName.lastIndexOf('.');

        if (lastDot < 0 || lastDot == fileName.length() - 1) {
            return "";
        }

        return fileName.substring(lastDot + 1)
                .toLowerCase(Locale.ROOT);
    }
}