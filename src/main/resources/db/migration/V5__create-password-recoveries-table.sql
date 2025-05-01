CREATE TABLE file_versions (
    id BIGSERIAL PRIMARY KEY,
    name VARCHAR(255) NOT NULL,
    mime_type VARCHAR(255),
    base64 TEXT NOT NULL,
    size BIGINT,
    created_at TIMESTAMP NOT NULL,
    user_id INTEGER REFERENCES users(id),
    folder_id INTEGER REFERENCES folders(id),
    file_id INTEGER REFERENCES files(id),
    version_number INTEGER NOT NULL
);