CREATE TABLE file_share_permissions (
    id SERIAL PRIMARY KEY,
    file_share_id SERIAL,
    user_id SERIAL,
    can_edit BOOLEAN DEFAULT FALSE,
    can_share BOOLEAN DEFAULT FALSE,
    FOREIGN KEY (file_share_id) REFERENCES file_shares(id) ON DELETE CASCADE,
    FOREIGN KEY (user_id) REFERENCES users(id) ON DELETE CASCADE,
    CONSTRAINT unique_share_user UNIQUE (file_share_id, user_id)
); 