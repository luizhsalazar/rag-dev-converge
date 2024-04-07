package br.gov.sc.ciasc.ragdevconverge.service;

import br.gov.sc.ciasc.ragdevconverge.repository.AlprRepository;
import br.gov.sc.ciasc.ragdevconverge.model.Alpr;
import org.springframework.ai.embedding.EmbeddingClient;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class AlprService {

    private final EmbeddingClient embeddingClient;
    private final AlprRepository alprRepository;

    public AlprService(EmbeddingClient embeddingClient, AlprRepository alprRepository) {
        this.embeddingClient = embeddingClient;
        this.alprRepository = alprRepository;
    }

    public List<Alpr> search(String prompt, int limit) {
        List<Double> promptEmbed = embeddingClient.embed(prompt);

        return alprRepository.search(promptEmbed.toString(), limit);
    }
}
