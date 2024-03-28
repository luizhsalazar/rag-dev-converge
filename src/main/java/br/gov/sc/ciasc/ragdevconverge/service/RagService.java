package br.gov.sc.ciasc.ragdevconverge.service;

import br.gov.sc.ciasc.ragdevconverge.model.LlamaResponse;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class RagService {

    private final VectorDbService vectorDbService;

    private final LlamaAiService llamaAiService;

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

    public RagService(VectorDbService vectorDbService, LlamaAiService llamaAiService) {
        this.vectorDbService = vectorDbService;
        this.llamaAiService = llamaAiService;
    }

    public LlamaResponse rag(String userQuery) {
        var placas = vectorDbService.buscaPlacas(userQuery, 5);
        List<String> listPlacas = placas.stream().map(placa -> String.format("%s\n", placa.content())).toList();
        String placasString = String.join("\n", listPlacas);

        String promptAumentado = String.format(PROMPT_AUMENTADO, placasString, userQuery);

        return llamaAiService.generateMessage(promptAumentado);
    }
}
