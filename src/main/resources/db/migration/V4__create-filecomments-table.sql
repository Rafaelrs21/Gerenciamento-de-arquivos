CREATE TABLE file_comments (
  id SERIAL PRIMARY KEY,
  content TEXT(65535),
  created_at TIMESTAMP,
  user_id SERIAL,
  file_id SERIAL
);