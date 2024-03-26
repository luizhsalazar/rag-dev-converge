-- Install the extension we just compiled
CREATE EXTENSION IF NOT EXISTS vector;

CREATE TABLE IF NOT EXISTS embeddings (
  id bigserial PRIMARY KEY,
  embedding vector(512),
  content TEXT,
  created_at timestamptz DEFAULT now()
);