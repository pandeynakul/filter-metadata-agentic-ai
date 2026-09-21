Enterprise Local-Vector RAG System with Metadata Filtering
==========================================================
A privacy-focused, zero-cost Retrieval-Augmented Generation (RAG) backend and frontend
architecture built with Spring Boot 3.3.4, Spring AI 2.0.1, and React (Vite + TypeScript).

This platform enables multi-tenant document ingestion, precise vector search with
structured metadata filtering,
and automated context synthesis using local embeddings and Google Gemini.
=====================================**=============================
🛠️ Tech StackLayerTechnology
Frontend: React, Vite, TypeScript
Backend Framework: Spring Boot 3.3.4, Spring AI 2.0.1
Vector Store: PostgreSQL + PGVector
Embeddings Model: Ollama (nomic-embed-text)
LLM Provider: Google Gemini API (gemini-1.5-flash)
=========================================**========================
Multi-Tenant Document Ingestion:
Dynamic chunking and embedding of PDF documents via PagePdfDocumentReader and TokenTextSplitter.
Zero-Cost Local Vector Storage:
Stored and indexed in PostgreSQL using the PGVector extension with locally running Ollama (nomic-embed-text) embeddings.
Strict Metadata Filtering: Runtime document isolation by tenantId, department, and confidential flags to enforce
security and access controls during vector search.

LLM Synthesis: Synthesizes context-grounded answers using Google Gemini (gemini-1.5-flash).
Modern Developer Workflow: Single-port frontend/backend integration with reactive status tracking, fully integrated into
IntelliJ.
===================================**===============================

PS C:\stock-viewer\filter-metadata-agentic-ai\frontend> npm run dev

> frontend@0.0.0 dev
> vite


VITE v8.3.0 ready in 580 ms

➜ Local:   http://localhost:5173/
➜ Network: use --host to expose
➜ press h + enter to show help
===================
Kill all active Node/Vite processes to release file locks on the node_modules folder.
Run this command in PowerShell:
#Stop-Process -Name "node" -Force -ErrorAction SilentlyContinue

With the processes terminated, retry deleting the folder:
#Remove-Item -Recurse -Force frontend

Generate the React project with the TypeScript template:
#npm create vite@latest frontend -- --template react-ts

Navigate into frontend, install packages, and start Vite:
#cd frontend
#npm install
#npm run dev
=======================