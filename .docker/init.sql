-- Install the extension we just compiled
CREATE EXTENSION IF NOT EXISTS vector;

CREATE TABLE IF NOT EXISTS embeddings_placas (
  id bigserial PRIMARY KEY,
  embedding vector(1536),
  content TEXT,
  created_at timestamptz DEFAULT now()
);