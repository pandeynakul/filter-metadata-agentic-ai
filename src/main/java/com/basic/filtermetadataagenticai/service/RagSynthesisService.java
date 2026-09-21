package com.basic.filtermetadataagenticai.service;

/*
 * Created by Ankul on 18-09-2026 09:32
 */


import com.basic.filtermetadataagenticai.config.LoggerConfig;
import lombok.extern.slf4j.Slf4j;
import org.springframework.ai.chat.client.ChatClient;
import org.springframework.ai.chat.client.advisor.vectorstore.QuestionAnswerAdvisor;
import org.springframework.ai.vectorstore.SearchRequest;
import org.springframework.ai.vectorstore.VectorStore;
import org.springframework.ai.vectorstore.filter.FilterExpressionBuilder;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;

import java.util.logging.Logger;

@Service
@Slf4j
public class RagSynthesisService {

    private static final Logger logger = LoggerConfig.getLogger(RagSynthesisService.class.getName());
    private final ChatClient geminiChatClient;
    private final VectorStore vectorStore;

    public RagSynthesisService(@Qualifier("geminiChatClient") ChatClient geminiChatClient, VectorStore vectorStore) {
        this.vectorStore = vectorStore;
        // Binds Gemini as default ChatModel
        this.geminiChatClient = geminiChatClient;
    }

    public String queryWithMetadataFilter(String userQuery, String tenantId, String department) {
        logger.info(">> >> queryWithMetadataFilter " + userQuery);
        // 1. Construct Type-Safe Metadata Filter Expression
        FilterExpressionBuilder b = new FilterExpressionBuilder();
        var filterExpression = b.and(
                b.eq("tenantId", tenantId),
                b.eq("department", department)
        ).build();
        // 2. Configure PGVector SearchRequest
        SearchRequest searchRequest = SearchRequest.builder()
                .topK(3)
                .similarityThreshold(0.6)
                .filterExpression(filterExpression)
                // Evaluated inside Postgres SQL JSONB
                .build();
        // 3. Synthesize response via Google Gemini using context-injected search
        return geminiChatClient.prompt()
                .user(userQuery)
                .advisors(
                        QuestionAnswerAdvisor.builder(vectorStore)
                                .searchRequest(searchRequest)
                                .build()
                ).call().content();
    }
}
