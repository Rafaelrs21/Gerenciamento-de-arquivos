CREATE TABLE password_recoveries (
    id SERIAL PRIMARY KEY,
    token VARCHAR(255),
    user_id INTEGER REFERENCES users(id)
);