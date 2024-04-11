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

    public List<Alpr> search(String promptEmbedding, int contextSize) {
        String query = "SELECT content, 1 - (embedding <=> :user_promt::vector) AS cosine_similarity " +
                "        FROM embeddings_placas " +
                "        ORDER BY cosine_similarity desc " +
                "        LIMIT :contextSize";

        var sql = jdbcClient.sql(query)
                .param("user_promt", promptEmbedding)
                .param("contextSize", contextSize);

        return sql.query(Alpr.class).list();
    }
}
