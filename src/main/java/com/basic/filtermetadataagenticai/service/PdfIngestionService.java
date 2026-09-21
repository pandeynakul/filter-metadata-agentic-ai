package com.basic.filtermetadataagenticai.service;

/*
 * Created by Ankul on 18-09-2026 17:42
 */


import com.basic.filtermetadataagenticai.config.LoggerConfig;
import org.springframework.ai.document.Document;
import org.springframework.ai.reader.pdf.PagePdfDocumentReader;
import org.springframework.ai.transformer.splitter.TokenTextSplitter;
import org.springframework.ai.vectorstore.VectorStore;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;
import java.util.Map;
import java.util.logging.Logger;

@Service
public class PdfIngestionService {
    private static final Logger logger = LoggerConfig.getLogger(PdfIngestionService.class.getName());
    private final VectorStore vectorStore;

    public PdfIngestionService(VectorStore vectorStore) {
        this.vectorStore = vectorStore;
    }

    public void processAndIngestUpload(
            MultipartFile file,
            String tenantId,
            String department,
            boolean confidential) {

        logger.info(">> >> inside the  processAndIngestUpload "
                + file + " " + tenantId + " " + "" + department);

        // Validate that the uploaded file is not empty
        if (file.isEmpty()) {
            throw new IllegalArgumentException("Cannot process an empty PDF file.");
        }
        // 1. Pass the MultipartFile Resource directly into Spring AI's PagePdfDocumentReader
        PagePdfDocumentReader pdfReader = new PagePdfDocumentReader(file.getResource());
        List<Document> pageDocuments = pdfReader.get();
        // 2. Chunk large documents into smaller token segments
        TokenTextSplitter splitter = TokenTextSplitter.builder()
                .withChunkSize(800)       // Target chunk size in tokens
                .withMinChunkSizeChars(350)     // Minimum character threshold per chunk
                .withMinChunkLengthToEmbed(5)   // Minimum token length required to generate embedding
                .withMaxNumChunks(10000)        // Upper limit on maximum chunk count
                .withKeepSeparator(true)        // Retain white space and sentence boundaries
                .build();
        List<Document> chunkedDocuments = splitter.apply(pageDocuments);

        // 3. Inject dynamic runtime metadata into each chunk
        chunkedDocuments.forEach(doc -> {
            Map<String, Object> metadata = doc.getMetadata();
            metadata.put("tenantId", tenantId);
            metadata.put("department", department);
            metadata.put("confidential", confidential);
            metadata.put("sourceFileName", file.getOriginalFilename());
            metadata.put("fileSize", file.getSize());
        });
        logger.info(">> >> before : vectorStore.accept(chunkedDocuments) ");
        // 4. Generate local embeddings with Ollama & persist to PGVector
        vectorStore.accept(chunkedDocuments);
    }
}
