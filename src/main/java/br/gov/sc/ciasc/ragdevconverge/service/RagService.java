package br.gov.sc.ciasc.ragdevconverge.service;

import br.gov.sc.ciasc.ragdevconverge.model.ChatResponse;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class RagService {

    private final AlprService alprService;

    private final ChatService chatService;

    private static final String PROMPT_AUMENTADO =
    """
            Você é um fiscal de transito que busca veiculos que cometeram alguma infracao de transito.
            Encontre os veiculos solicitados. Utilize o contexto abaixo para a sua resposta.
            Context: %s
            Query:  %s
    """;

//    private static final SystemMessage SYSTEM_MESSAGE = new SystemMessage(
//            """
//                    You're an assistant who helps to find lodging in San Francisco.
//                    Suggest three options. Send back a JSON object in the format below.
//                    [{\"name\": \"<hotel name>\", \"description\": \"<hotel description>\", \"price\": <hotel price>}]
//                    Don't add any other text to the response. Don't add the new line or any other symbols to the response. Send back the raw JSON.
//                    """);

    public RagService(AlprService alprService, ChatService chatService) {
        this.alprService = alprService;
        this.chatService = chatService;
    }

    public ChatResponse rag(String userQuery) {
        var placas = alprService.buscaPlacas(userQuery, 5);
        List<String> listPlacas = placas.stream().map(placa -> String.format("%s\n", placa.content())).toList();
        String placasString = String.join("\n", listPlacas);

        String promptAumentado = String.format(PROMPT_AUMENTADO, placasString, userQuery);

        return chatService.generateMessage(promptAumentado);
    }
}
