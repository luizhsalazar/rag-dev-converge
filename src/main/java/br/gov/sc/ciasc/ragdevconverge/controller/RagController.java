package br.gov.sc.ciasc.ragdevconverge.controller;

import br.gov.sc.ciasc.ragdevconverge.model.RagResponse;
import br.gov.sc.ciasc.ragdevconverge.service.MemoryRagService;
import br.gov.sc.ciasc.ragdevconverge.service.RagService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import reactor.core.publisher.Flux;

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
    public ResponseEntity<RagResponse> rag(@RequestParam String userQuery, @RequestParam(required = false, defaultValue = "10") int contextSize) {
        var rag = ragService.rag(userQuery, contextSize);
        return ResponseEntity.ok(rag);
    }

    @GetMapping("/stream")
    public Flux<String> ragStream(@RequestParam String userQuery) {
        return ragService.ragStream(userQuery);
    }

    // todo: Rever se faz sentido manter essa rota para exemplo
    @GetMapping("/with-memory")
    public ResponseEntity<RagResponse> ragWithMemory(@RequestParam String userQuery) {
        var rag = memoryRagService.ragWithMemory(userQuery);
        return ResponseEntity.ok(rag);
    }
}
