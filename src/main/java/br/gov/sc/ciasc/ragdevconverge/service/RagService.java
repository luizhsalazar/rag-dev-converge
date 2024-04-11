package br.gov.sc.ciasc.ragdevconverge.service;

import br.gov.sc.ciasc.ragdevconverge.model.RagResponse;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.ai.chat.ChatResponse;
import org.springframework.ai.chat.messages.Message;
import org.springframework.ai.chat.messages.UserMessage;
import org.springframework.ai.chat.prompt.Prompt;
import org.springframework.ai.chat.prompt.PromptTemplate;
import org.springframework.ai.chat.prompt.SystemPromptTemplate;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.io.Resource;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Flux;

import java.util.List;
import java.util.Map;

@Service
public class RagService {

    private static final Logger log = LoggerFactory.getLogger(RagService.class);

    private final AlprService alprService;

    private final ChatService chatService;

    @Value("classpath:/prompts/rag-prompt-template.st")
    private Resource ragPromptTemplate;

    public RagService(AlprService alprService, ChatService chatService) {
        this.alprService = alprService;
        this.chatService = chatService;
    }

    public RagResponse rag(String userQuery, int contextSize) {
        Prompt augmentedPrompt = getAugmentedPrompt(userQuery, contextSize);
        return chatService.generateMessage(augmentedPrompt);
    }

    public Flux<String> ragStream(String userQuery) {
        Prompt promptAumentado = getAugmentedPrompt(userQuery, 10);
        Flux<ChatResponse> chatResponseFlux = chatService.generateMessageStream(promptAumentado);
        return chatResponseFlux.mapNotNull(chatResponse -> chatResponse.getResult().getOutput().getContent());
    }

    private Prompt getAugmentedPrompt(String userQuery, int contextSize) {
        var alprData = alprService.search(userQuery, contextSize);
        List<String> alprDataList = alprData.stream().map(alpr -> String.format("%s\n", alpr.content())).toList();
        String context = String.join("\n", alprDataList);

        // SYSTEM role
        PromptTemplate systemPromptTemplate = new SystemPromptTemplate(ragPromptTemplate);
        Message systemMessage = systemPromptTemplate.createMessage(Map.of("contexto", context));

        // USER role
        UserMessage userMessage = new UserMessage(userQuery);
        Prompt augmentedPrompt = new Prompt(List.of(systemMessage, userMessage));
        log.info(String.valueOf(augmentedPrompt));

        return augmentedPrompt;

        /* Opção de incluir o tudo em um template que fica apenas com ROLE de "UserMessage"
        PromptTemplate promptTemplate = new PromptTemplate(ragPromptTemplate);
        Map<String, Object> promptParameters = new HashMap<>();
        promptParameters.put("query", userQuery);
        promptParameters.put("contexto", context);

        Prompt augmentedPrompt = promptTemplate.create(promptParameters);
        log.info(String.valueOf(augmentedPrompt));
        return augmentedPrompt;
         */
    }
}
