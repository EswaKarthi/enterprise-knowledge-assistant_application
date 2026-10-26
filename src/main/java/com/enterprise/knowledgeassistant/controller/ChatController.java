package com.enterprise.knowledgeassistant.controller;


import com.enterprise.knowledgeassistant.dto.request.ChatRequest;
import com.enterprise.knowledgeassistant.dto.response.ChatResponse;
import com.enterprise.knowledgeassistant.service.ChatService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/v1/chat")
@RequiredArgsConstructor
public class ChatController {

    private final ChatService chatService;

    @PostMapping
    public ChatResponse chat(@RequestBody ChatRequest request) {
        return chatService.chat(request);
    }
}