CREATE TABLE jwt_session (
                             user_id UUID PRIMARY KEY,
                             access_token VARCHAR(512) NOT NULL,
                             refresh_token VARCHAR(512) NOT NULL,
                             updated_at timestamp with time zone NOT NULL,
                             created_at timestamp with time zone NOT NULL,
                             CONSTRAINT fk_jwt_session_user FOREIGN KEY (user_id) REFERENCES users(id)
);