CREATE TABLE file_user_permissions
(
    permission VARCHAR(255) NOT NULL,
    created_at TIMESTAMP WITHOUT TIME ZONE,
    file_id    BIGINT       NOT NULL,
    user_id    BIGINT       NOT NULL,
    CONSTRAINT pk_file_user_permissions PRIMARY KEY (file_id, user_id)
);

ALTER TABLE file_user_permissions
    ADD CONSTRAINT FK_FILE_USER_PERMISSIONS_ON_FILE FOREIGN KEY (file_id) REFERENCES files (id);

ALTER TABLE file_user_permissions
    ADD CONSTRAINT FK_FILE_USER_PERMISSIONS_ON_USER FOREIGN KEY (user_id) REFERENCES users (id);