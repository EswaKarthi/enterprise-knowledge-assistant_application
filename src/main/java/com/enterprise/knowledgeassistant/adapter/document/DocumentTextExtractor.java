package com.enterprise.knowledgeassistant.adapter.document;

import org.apache.tika.Tika;
import org.apache.tika.exception.TikaException;
import org.springframework.stereotype.Component;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.io.InputStream;

@Component
public class DocumentTextExtractor {

    private final Tika tika = new Tika();

    public String extractText(MultipartFile file) {

        try (InputStream inputStream = file.getInputStream()) {

            String extractedText = tika.parseToString(inputStream);

            if (extractedText == null || extractedText.isBlank()) {
                throw new IllegalArgumentException(
                        "No readable text found in the uploaded document"
                );
            }

            return extractedText.trim();

        } catch (IOException | TikaException exception) {

            throw new IllegalArgumentException(
                    "Failed to extract text from the uploaded document",
                    exception
            );
        }
    }
}