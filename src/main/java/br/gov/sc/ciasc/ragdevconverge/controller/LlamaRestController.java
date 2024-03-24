package br.gov.sc.ciasc.ragdevconverge.controller;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/chat")
public class LlamaRestController {

    @GetMapping()
    public String chat() {
        return "auuur";
    }
}
