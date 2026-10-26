package com.enterprise.knowledgeassistant.agent.tools;

import com.enterprise.knowledgeassistant.service.RetrievalService;
import lombok.RequiredArgsConstructor;
import org.springframework.ai.document.Document;
import org.springframework.ai.tool.annotation.Tool;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
@RequiredArgsConstructor
public class CandidateSearchTool {

    private final RetrievalService retrievalService;

    @Tool(description = """
            Search the knowledge base for candidate resumes
            and retrieve relevant document chunks.

            Use this tool when the user asks to find candidates,
            search resumes, or identify candidate experience
            and technical skills.

            The query should describe the skills, experience,
            or qualifications that need to be searched.
            """)
    public List<String> searchCandidates(String query) {

        List<Document> documents =
                retrievalService.retrieveRelevantChunks(query);

        return documents.stream()
                .map(Document::getText)
                .toList();
    }
}