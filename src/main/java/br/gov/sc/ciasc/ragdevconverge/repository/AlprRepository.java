package br.gov.sc.ciasc.ragdevconverge.repository;

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

    public List<Alpr> search(String promptEmbedding, int limit) {
        String query = "SELECT content, 1 - (embedding <=> :user_promt::vector) AS cosine_similarity " +
                "        FROM embeddings_placas_anon " +
                "        ORDER BY cosine_similarity desc " +
                "        LIMIT :limit";

        var sql = jdbcClient.sql(query)
                .param("user_promt", promptEmbedding)
                .param("limit", limit);

        return sql.query(Alpr.class).list();
    }
}
