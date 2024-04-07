package br.gov.sc.ciasc.ragdevconverge.service;

import br.gov.sc.ciasc.ragdevconverge.model.RagResponse;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.ai.chat.ChatClient;
import org.springframework.ai.chat.ChatResponse;
import org.springframework.ai.chat.StreamingChatClient;
import org.springframework.ai.chat.prompt.Prompt;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Flux;

@Service
public class ChatService {

    private static final Logger log = LoggerFactory.getLogger(ChatService.class);

    private final ChatClient chatClient;
    private final StreamingChatClient streamingChatClient;

    public ChatService(ChatClient chatClient, StreamingChatClient streamingChatClient) {
        this.chatClient = chatClient;
        this.streamingChatClient = streamingChatClient;
    }

    public RagResponse generateMessage(String userQuery) {
        final String response = chatClient.call(userQuery);
        return new RagResponse(response);
    }

    public RagResponse generateMessage(Prompt prompt) {
        ChatResponse chatResponse = chatClient.call(prompt);
        log.info(String.valueOf(chatResponse));
        return new RagResponse(chatResponse.getResult().getOutput().getContent());
    }

    public Flux<String> generateMessageStream(String userQuery) {
        return streamingChatClient.stream(userQuery);
    }

    public Flux<ChatResponse> generateMessageStream(Prompt prompt) {
        return streamingChatClient.stream(prompt);
    }
}
