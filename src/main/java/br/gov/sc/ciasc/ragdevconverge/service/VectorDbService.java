package br.gov.sc.ciasc.ragdevconverge.service;

import br.gov.sc.ciasc.ragdevconverge.model.Placa;
import org.springframework.ai.embedding.EmbeddingClient;
import org.springframework.jdbc.core.simple.JdbcClient;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class VectorDbService {
    private final JdbcClient jdbcClient;
    private final EmbeddingClient aiClient;

    public VectorDbService(JdbcClient jdbcClient, EmbeddingClient aiClient) {
        this.jdbcClient = jdbcClient;
        this.aiClient = aiClient;
    }

    public List<Placa> buscaPlacas(String prompt) {
        List<Double> promptEmbedding = aiClient.embed(prompt);

        JdbcClient.StatementSpec query = jdbcClient.sql(
                "SELECT content, 1 - (embedding <=> :user_promt::vector) AS cosine_similarity " +
                        "        FROM embeddings_alpr " +
                        "        ORDER BY cosine_similarity desc " +
                        "        LIMIT 5")
                .param("user_promt", promptEmbedding.toString());

        return query.query(Placa.class).list();
    }
}
