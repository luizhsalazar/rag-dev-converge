package br.gov.sc.ciasc.ragdevconverge.service;

import br.gov.sc.ciasc.ragdevconverge.model.LlamaResponse;
import org.springframework.ai.chat.ChatClient;
import org.springframework.ai.chat.StreamingChatClient;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Flux;

@Service
public class LlamaAiService {

    private final ChatClient chatClient;
    private final StreamingChatClient streamingChatClient;

    public LlamaAiService(ChatClient chatClient, StreamingChatClient streamingChatClient) {
        this.chatClient = chatClient;
        this.streamingChatClient = streamingChatClient;
    }

    public LlamaResponse generateMessage(String userQuery) {
        final String llamaMessage = chatClient.call(userQuery);
        return new LlamaResponse(llamaMessage);
    }

    public Flux<String> generateMessageStream(String userQuery) {
        return streamingChatClient.stream(userQuery);
    }
}
