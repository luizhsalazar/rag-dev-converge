package br.gov.sc.ciasc.ragdevconverge.controller;

import br.gov.sc.ciasc.ragdevconverge.model.LlamaResponse;
import br.gov.sc.ciasc.ragdevconverge.model.Placa;
import br.gov.sc.ciasc.ragdevconverge.service.LlamaAiService;
import br.gov.sc.ciasc.ragdevconverge.service.VectorDbService;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import reactor.core.publisher.Flux;

import java.util.List;

@RestController
@RequestMapping("/api/chat")
public class LlamaRestController {

    private final LlamaAiService llamaAiService;
    private final VectorDbService vectorDbService;

    public LlamaRestController(LlamaAiService llamaAiService1, VectorDbService vectorDbService) {
        this.llamaAiService = llamaAiService1;
        this.vectorDbService = vectorDbService;
    }

    @GetMapping()
    public ResponseEntity<LlamaResponse> generateMessage(@RequestParam String userQuery) {
        LlamaResponse llamaResponse = llamaAiService.generateMessage(userQuery);
        return ResponseEntity.status(HttpStatus.OK).body(llamaResponse);
    }

    @GetMapping(value = "/stream")
    public Flux<String> generateMessageStream(@RequestParam String userQuery) {
        return llamaAiService.generateMessageStream(userQuery);
    }

    @GetMapping(value = "/placas")
    public List<Placa> buscaPlacas(@RequestParam String userQuery) {
        return vectorDbService.buscaPlacas(userQuery, 5);
    }
}
