package br.gov.sc.ciasc.ragdevconverge.service;

import br.gov.sc.ciasc.ragdevconverge.model.ChatResponse;
import org.springframework.ai.chat.ChatClient;
import org.springframework.ai.chat.StreamingChatClient;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Flux;

@Service
public class ChatService {

    private final ChatClient chatClient;
    private final StreamingChatClient streamingChatClient;

    public ChatService(ChatClient chatClient, StreamingChatClient streamingChatClient) {
        this.chatClient = chatClient;
        this.streamingChatClient = streamingChatClient;
    }

    public ChatResponse generateMessage(String userQuery) {
        final String llamaMessage = chatClient.call(userQuery);
        return new ChatResponse(llamaMessage);
    }

    public Flux<String> generateMessageStream(String userQuery) {
        return streamingChatClient.stream(userQuery);
    }
}
