package br.gov.sc.ciasc.ragdevconverge;

import br.gov.sc.ciasc.ragdevconverge.model.Alpr;
import org.springframework.jdbc.core.simple.JdbcClient;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public class AlprRepository {

    private final JdbcClient jdbcClient;

    public AlprRepository(JdbcClient jdbcClient) {
        this.jdbcClient = jdbcClient;
    }

    public List<Alpr> buscaPlacas(String promptEmbedding, int limit) {
        JdbcClient.StatementSpec query = jdbcClient.sql(
                        "SELECT content, 1 - (embedding <=> :user_promt::vector) AS cosine_similarity " +
                                "        FROM embeddings_placas_anon " +
                                "        ORDER BY cosine_similarity desc " +
                                "        LIMIT :limit")
                .param("user_promt", promptEmbedding)
                .param("limit", limit);

        return query.query(Alpr.class).list();
    }
}
