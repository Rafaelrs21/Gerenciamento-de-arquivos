CREATE TABLE password_recoveries (
     id SERIAL PRIMARY KEY,
     token VARCHAR(255),
     user_id INTEGER REFERENCES users(id),
     is_used BOOLEAN NOT NULL,
     expires_at TIMESTAMP NOT NULL
 );