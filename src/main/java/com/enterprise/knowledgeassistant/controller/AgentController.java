package com.enterprise.knowledgeassistant.controller;

import com.enterprise.knowledgeassistant.dto.request.AgentRequest;
import com.enterprise.knowledgeassistant.dto.response.AgentResponse;
import com.enterprise.knowledgeassistant.service.AgentService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/v1/agent")
@RequiredArgsConstructor
public class AgentController {

    private final AgentService agentService;

    @PostMapping
    public AgentResponse execute(@RequestBody AgentRequest request) {

        String answer = agentService.execute(request.message());

        return new AgentResponse(answer);
    }
}