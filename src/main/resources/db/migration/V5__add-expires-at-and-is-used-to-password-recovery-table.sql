ALTER TABLE password_recoveries ADD COLUMN expires_at TIMESTAMP NOT NULL;

ALTER TABLE password_recoveries ADD COLUMN is_used BOOLEAN NOT NULL;