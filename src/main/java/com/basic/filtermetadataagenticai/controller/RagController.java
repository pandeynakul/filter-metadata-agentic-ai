package com.basic.filtermetadataagenticai.controller;

/*
 * Created by Ankul on 18-09-2026 10:11
 */


import com.basic.filtermetadataagenticai.dto.QueryRequest;
import com.basic.filtermetadataagenticai.service.DocumentIngestionService;
import com.basic.filtermetadataagenticai.service.RagSynthesisService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

@RestController
@RequestMapping("/api/v1/rag")
public class RagController {

    private final DocumentIngestionService ingestionService;
    private final RagSynthesisService ragService;

    public RagController(DocumentIngestionService ingestionService, RagSynthesisService ragService) {
        this.ingestionService = ingestionService;
        this.ragService = ragService;
    }

    @PostMapping("/ingest")
    public ResponseEntity<String> ingest() {
        ingestionService.ingestSampleDocuments();
        return ResponseEntity.ok("Documents ingested and embedded locally.");
    }

    @PostMapping("/query")
    public ResponseEntity<Map<String, String>> query(
            @RequestHeader("X-Tenant-ID") String tenantId,
            @RequestParam String department,
            @RequestBody QueryRequest request) {

        String answer = ragService.queryWithMetadataFilter(request.prompt(), tenantId, department);
        return ResponseEntity.ok(Map.of("answer", answer));
    }


}
