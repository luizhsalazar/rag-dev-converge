package br.gov.sc.ciasc.ragdevconverge.service;

import br.gov.sc.ciasc.ragdevconverge.model.LlamaResponse;
import org.springframework.ai.chat.ChatClient;
import org.springframework.stereotype.Service;

@Service
public class LlamaAiService {

    private final ChatClient chatClient;

    public LlamaAiService(ChatClient chatClient) {
        this.chatClient = chatClient;
    }

    public LlamaResponse generateMessage(String userQuery) {
        final String llamaMessage = chatClient.call(userQuery);
        return new LlamaResponse(llamaMessage);
    }
}
