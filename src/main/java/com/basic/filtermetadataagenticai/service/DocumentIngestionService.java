package com.basic.filtermetadataagenticai.service;

/*
 * Created by Ankul on 17-09-2026 17:28
 */

import org.springframework.ai.document.Document;
import org.springframework.ai.vectorstore.VectorStore;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;

@Service
public class DocumentIngestionService {

    private final VectorStore vectorStore;

    public DocumentIngestionService(VectorStore vectorStore) {
        this.vectorStore = vectorStore;
    }

    public void ingestSampleDocuments() {
        List<Document> docs = List.of(
                new Document("Q3 Financial Overview: Revenue grew by 15% YoY.", Map.of(
                        "tenantId", "acme-corp",
                        "department", "finance",
                        "year", 2024,
                        "confidential", true
                )),
                new Document("Engineering Roadmap: Shifting workloads to K8s.", Map.of(
                        "tenantId", "acme-corp",
                        "department", "engineering",
                        "year", 2024,
                        "confidential", false
                )),
                new Document("Q3 Financial Overview: Revenue declined by 2% YoY.", Map.of(
                        "tenantId", "stark-ind",
                        "department", "finance",
                        "year", 2024,
                        "confidential", true
                ))
        );
        // Ollama generates local 768d embeddings and writes metadata to Postgres JSONB
        vectorStore.accept(docs);
    }
}
