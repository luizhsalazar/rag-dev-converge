package br.gov.sc.ciasc.ragdevconverge.service;

import br.gov.sc.ciasc.ragdevconverge.model.Placa;
import org.springframework.ai.embedding.EmbeddingClient;
import org.springframework.jdbc.core.simple.JdbcClient;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class VectorDbService {
    private final JdbcClient jdbcClient;
    private final EmbeddingClient embeddingClient;

    public VectorDbService(JdbcClient jdbcClient, EmbeddingClient embeddingClient) {
        this.jdbcClient = jdbcClient;
        this.embeddingClient = embeddingClient;
    }

    public List<Placa> buscaPlacas(String prompt, int limit) {
        List<Double> promptEmbedding = embeddingClient.embed(prompt);

        JdbcClient.StatementSpec query = jdbcClient.sql(
                "SELECT content, 1 - (embedding <=> :user_promt::vector) AS cosine_similarity " +
                        "        FROM embeddings_placas_anon " +
                        "        ORDER BY cosine_similarity desc " +
                        "        LIMIT :limit")
                .param("user_promt", promptEmbedding.toString())
                .param("limit", limit);

        return query.query(Placa.class).list();
    }
}
