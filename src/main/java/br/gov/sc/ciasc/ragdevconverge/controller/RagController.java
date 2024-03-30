package br.gov.sc.ciasc.ragdevconverge.controller;

import br.gov.sc.ciasc.ragdevconverge.model.LlamaResponse;
import br.gov.sc.ciasc.ragdevconverge.service.MemoryRagService;
import br.gov.sc.ciasc.ragdevconverge.service.RagService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/rag")
public class RagController {

    private final RagService ragService;
    private final MemoryRagService memoryRagService;

    public RagController(RagService ragService, MemoryRagService memoryRagService) {
        this.ragService = ragService;
        this.memoryRagService = memoryRagService;
    }

    @GetMapping()
    public ResponseEntity<LlamaResponse> ragPlacas(@RequestParam String userQuery) {
        var rag = ragService.rag(userQuery);
        return ResponseEntity.ok(rag);
    }

    @GetMapping("/with-memory")
    public ResponseEntity<LlamaResponse> ragPlacasWithMemory(@RequestParam String userQuery) {
        var rag = memoryRagService.rag(userQuery);
        return ResponseEntity.ok(rag);
    }
}
