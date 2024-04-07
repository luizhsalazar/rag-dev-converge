package br.gov.sc.ciasc.ragdevconverge.service;

import br.gov.sc.ciasc.ragdevconverge.model.RagResponse;
import dev.langchain4j.data.message.AiMessage;
import dev.langchain4j.data.message.UserMessage;
import dev.langchain4j.memory.ChatMemory;
import dev.langchain4j.memory.chat.MessageWindowChatMemory;
import dev.langchain4j.model.chat.ChatLanguageModel;
import dev.langchain4j.model.openai.OpenAiChatModel;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class MemoryRagService {

    private final AlprService alprService;

    private final ChatMemory chatMemory;

    @Value("${openai.api-key}")
    private String openAiKey;

    private static final String PROMPT_AUMENTADO =
    """
            Você é um fiscal de transito que busca veiculos que cometeram alguma infracao de transito.
            Encontre os veiculos solicitados. Utilize o contexto abaixo para a sua resposta.
            Context: %s
            Query:  %s
    """;

    public MemoryRagService(AlprService alprService) {
        this.alprService = alprService;
        chatMemory = MessageWindowChatMemory.withMaxMessages(20);
    }

    public RagResponse ragWithMemory(String userQuery) {
        ChatLanguageModel model = OpenAiChatModel.withApiKey(openAiKey);
        var userPrompt = userQuery;

        if (chatMemory.messages().isEmpty()) {
            var placas = alprService.search(userPrompt, 5);
            List<String> listPlacas = placas.stream().map(placa -> String.format("%s\n", placa.content())).toList();
            String placasString = String.join("\n", listPlacas);

            userPrompt = String.format(PROMPT_AUMENTADO, placasString, userQuery);
        }

        chatMemory.add(UserMessage.userMessage(userPrompt));
        AiMessage answer = model.generate(chatMemory.messages()).content();
        chatMemory.add(answer);

        return new RagResponse(answer.text());
    }
}
