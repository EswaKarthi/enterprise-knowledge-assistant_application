package com.enterprise.knowledgeassistant.serviceImpl;

import com.enterprise.knowledgeassistant.adapter.ai.chat.ChatClient;
import com.enterprise.knowledgeassistant.dto.request.ChatRequest;
import com.enterprise.knowledgeassistant.dto.response.ChatResponse;
import com.enterprise.knowledgeassistant.service.ChatService;
import com.enterprise.knowledgeassistant.service.RetrievalService;
import lombok.RequiredArgsConstructor;
import org.springframework.ai.document.Document;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class ChatServiceImpl implements ChatService {

    private final RetrievalService retrievalService;
    private final ChatClient chatClient;

    @Override
    public ChatResponse chat(ChatRequest request) {

        if (request == null
                || request.getQuestion() == null
                || request.getQuestion().isBlank()) {

            throw new IllegalArgumentException(
                    "Question must not be empty");
        }

        String question = request.getQuestion();

        // Step 1: Retrieve relevant document chunks
        List<Document> relevantChunks =
                retrievalService.retrieveRelevantChunks(question);

        // Step 2: Handle empty retrieval results
        if (relevantChunks == null || relevantChunks.isEmpty()) {

            return new ChatResponse(
                    "I could not find relevant information in the available documents."
            );
        }

        // Step 3: Build context from retrieved chunks
        String context = relevantChunks.stream()
                .map(Document::getText)
                .collect(Collectors.joining("\n\n"));

        // Step 4: Build the prompt
        String prompt = buildPrompt(question, context);

        // Step 5: Generate the answer using Gemini
        String answer = chatClient.generateAnswer(prompt);

        // Step 6: Return ChatResponse
        return new ChatResponse(answer);
    }

    private String buildPrompt(String question, String context) {

        return """
                You are an enterprise knowledge assistant.

                Answer the user's question using the provided document context.

                Instructions:
                1. Use the context as the factual basis for your answer.
                2. Do not invent facts.
                3. If the context does not contain sufficient information,
                   state that the answer cannot be determined
                   from the available documents.

                Document Context:
                %s

                User Question:
                %s

                Answer:
                """.formatted(context, question);
    }
}