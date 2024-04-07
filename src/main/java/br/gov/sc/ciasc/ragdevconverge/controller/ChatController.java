package br.gov.sc.ciasc.ragdevconverge.controller;

import br.gov.sc.ciasc.ragdevconverge.model.RagResponse;
import br.gov.sc.ciasc.ragdevconverge.service.ChatService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import reactor.core.publisher.Flux;

@RestController
@RequestMapping("/api/chat")
public class ChatController {

    private final ChatService chatService;

    public ChatController(ChatService chatService) {
        this.chatService = chatService;
    }

    @GetMapping()
    public ResponseEntity<RagResponse> generateMessage(@RequestParam String userQuery) {
        RagResponse ragResponse = chatService.generateMessage(userQuery);
        return ResponseEntity.status(HttpStatus.OK).body(ragResponse);
    }

    @GetMapping(value = "/stream")
    public Flux<String> generateMessageStream(@RequestParam String userQuery) {
        return chatService.generateMessageStream(userQuery);
    }
}
