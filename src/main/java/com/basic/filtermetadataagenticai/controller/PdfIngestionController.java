package com.basic.filtermetadataagenticai.controller;

/*
 * Created by Ankul on 18-09-2026 18:08
 */

import com.basic.filtermetadataagenticai.service.PdfIngestionService;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.Map;

@RestController
@RequestMapping("/api/v1/rag")
@CrossOrigin(origins = "http://localhost:5173")
public class PdfIngestionController {

    private final PdfIngestionService pdfIngestionService;

    public PdfIngestionController(PdfIngestionService pdfIngestionService) {
        this.pdfIngestionService = pdfIngestionService;
    }

    @PostMapping(value = "/upload-pdf", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public ResponseEntity<Map<String, String>> uploadAndIngestPdf(
            @RequestPart("file") MultipartFile file,
            @RequestParam("tenantId") String tenantId,
            @RequestParam("department") String department,
            @RequestParam(value = "confidential", defaultValue = "false") boolean confidential) {
        pdfIngestionService.processAndIngestUpload(file, tenantId, department, confidential);
        return ResponseEntity.ok(Map.of(
                "status", "success",
                "fileName", file.getOriginalFilename(),
                "message", "PDF successfully parsed, chunked, and stored in PGVector with metadata."
        ));
    }
}
