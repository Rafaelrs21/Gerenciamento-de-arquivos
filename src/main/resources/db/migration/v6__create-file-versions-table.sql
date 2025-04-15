CREATE TABLE file_versions (
    id BIGINT AUTO_INCREMENT PRIMARY KEY,
    file_id BIGINT NOT NULL,
    version_number INT NOT NULL,
    comment TEXT,
    CONSTRAINT fk_file_versions_file_id FOREIGN KEY (file_id) REFERENCES files(id) ON DELETE CASCADE
);

