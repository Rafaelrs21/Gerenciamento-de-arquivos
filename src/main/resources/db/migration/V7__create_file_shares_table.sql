CREATE TABLE file_shares (
    id SERIAL PRIMARY KEY,
    created_at TIMESTAMP,
    expires_at TIMESTAMP,
    owner_id SERIAL,
    file_id SERIAL,
    share_seed VARCHAR(8),
    publico BOOLEAN DEFAULT FALSE,
    temporario BOOLEAN DEFAULT FALSE,
    FOREIGN KEY (owner_id) REFERENCES users(id),
    FOREIGN KEY (file_id) REFERENCES files(id),
    CONSTRAINT unique_active_file_share UNIQUE (file_id, owner_id)
); 