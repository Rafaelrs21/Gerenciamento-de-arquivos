CREATE TABLE file_versions (
    id SERIAL PRIMARY KEY,
    file_id BIGINT NOT NULL,
    version_number INT NOT NULL,
    name VARCHAR(255) NOT NULL,
    mime_type VARCHAR(255) NOT NULL,
    base64 TEXT NOT NULL,
    size BIGINT NOT NULL,
    created_at TIMESTAMP NOT NULL,
    CONSTRAINT fk_file_versions_file_id FOREIGN KEY (file_id) REFERENCES files(id) ON DELETE CASCADE
);
