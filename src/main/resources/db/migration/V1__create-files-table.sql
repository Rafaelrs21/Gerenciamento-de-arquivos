CREATE TABLE files (
    id SERIAL PRIMARY KEY,
    name VARCHAR(255) NOT NULL,
    mime_type VARCHAR(255),
    base64 TEXT,
    size BIGINT,
    created_at TIMESTAMP,
    user_id BIGINT,
    folder_id BIGINT
);