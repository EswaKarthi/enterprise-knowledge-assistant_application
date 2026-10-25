package com.enterprise.knowledgeassistant;

import org.junit.jupiter.api.Test;
import org.springframework.ai.vectorstore.VectorStore;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.bean.override.mockito.MockitoBean;

@SpringBootTest
@ActiveProfiles("test")
class EnterpriseKnowledgeAssistantApplicationTests {

	@MockitoBean
	private VectorStore vectorStore;

	@Test
	void contextLoads() {
	}

}
