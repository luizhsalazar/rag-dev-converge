package br.gov.sc.ciasc.ragdevconverge.controller;

import br.gov.sc.ciasc.ragdevconverge.model.LlamaResponse;
import br.gov.sc.ciasc.ragdevconverge.service.LlamaAiService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/chat")
public class LlamaRestController {

    private final LlamaAiService llamaAiService;

    public LlamaRestController(LlamaAiService llamaAiService) {
        this.llamaAiService = llamaAiService;
    }

    @GetMapping()
    public ResponseEntity<LlamaResponse> generateMessage(@RequestParam String userQuery) {
        LlamaResponse llamaResponse = llamaAiService.generateMessage(userQuery);
        return ResponseEntity.status(HttpStatus.OK).body(llamaResponse);
    }
}
