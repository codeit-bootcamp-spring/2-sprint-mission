-- 기존 테이블 제거 (의존성의 역순으로)
DROP TABLE IF EXISTS message_attachments;
DROP TABLE IF EXISTS messages;
DROP TABLE IF EXISTS read_statuses;
DROP TABLE IF EXISTS user_statuses;
DROP TABLE IF EXISTS channels;
DROP TABLE IF EXISTS users;
DROP TABLE IF EXISTS binary_contents;

-- binary_contents 테이블 생성
CREATE TABLE binary_contents (
                                 uuid UUID PRIMARY KEY,
                                 id VARCHAR,
                                 created_at TIMESTAMPTZ NOT NULL,
                                 file_name VARCHAR(255) NOT NULL,
                                 size BIGINT NOT NULL,
                                 content_type VARCHAR(100) NOT NULL,
                                 bytes BYTEA NOT NULL
);

-- users 테이블 생성
CREATE TABLE users (
                       uuid UUID PRIMARY KEY,
                       id VARCHAR,
                       created_at TIMESTAMPTZ NOT NULL,
                       updated_at TIMESTAMPTZ,
                       username VARCHAR(50) UNIQUE NOT NULL,
                       email VARCHAR(100) UNIQUE NOT NULL,
                       password VARCHAR(60) NOT NULL,
                       profile_id UUID,
                       CONSTRAINT fk_profile FOREIGN KEY (profile_id) REFERENCES binary_contents(uuid) ON DELETE SET NULL
);

-- channels 테이블 생성
CREATE TABLE channels (
                          uuid UUID PRIMARY KEY,
                          id VARCHAR,
                          created_at TIMESTAMPTZ NOT NULL,
                          updated_at TIMESTAMPTZ,
                          name VARCHAR(100),
                          description VARCHAR(500),
                          type VARCHAR(10) NOT NULL,
                          CONSTRAINT check_channel_type CHECK (type IN ('PUBLIC', 'PRIVATE'))
);

-- user_statuses 테이블 생성
CREATE TABLE user_statuses (
                               uuid UUID PRIMARY KEY,
                               id VARCHAR,
                               created_at TIMESTAMPTZ NOT NULL,
                               updated_at TIMESTAMPTZ,
                               user_id UUID NOT NULL,
                               last_active_at TIMESTAMPTZ NOT NULL,
                               CONSTRAINT fk_user_status_user FOREIGN KEY (user_id) REFERENCES users(uuid) ON DELETE CASCADE
);

-- read_statuses 테이블 생성
CREATE TABLE read_statuses (
                               uuid UUID PRIMARY KEY,
                               id VARCHAR,
                               created_at TIMESTAMPTZ NOT NULL,
                               updated_at TIMESTAMPTZ,
                               user_id UUID NOT NULL,
                               channel_id UUID NOT NULL,
                               last_read_at TIMESTAMPTZ NOT NULL,
                               CONSTRAINT fk_read_status_user FOREIGN KEY (user_id) REFERENCES users(uuid) ON DELETE CASCADE,
                               CONSTRAINT fk_read_status_channel FOREIGN KEY (channel_id) REFERENCES channels(uuid) ON DELETE CASCADE
);

-- messages 테이블 생성
CREATE TABLE messages (
                          uuid UUID PRIMARY KEY,
                          id VARCHAR,
                          created_at TIMESTAMPTZ NOT NULL,
                          updated_at TIMESTAMPTZ,
                          content TEXT,
                          channel_id UUID NOT NULL,
                          author_id UUID,
                          CONSTRAINT fk_message_channel FOREIGN KEY (channel_id) REFERENCES channels(uuid) ON DELETE CASCADE,
                          CONSTRAINT fk_message_author FOREIGN KEY (author_id) REFERENCES users(uuid) ON DELETE SET NULL
);

-- message_attachments 테이블 생성
CREATE TABLE message_attachments (
                                     uuid UUID PRIMARY KEY,
                                     message_id UUID NOT NULL,
                                     attachment_id UUID NOT NULL,
                                     CONSTRAINT fk_attachment_message FOREIGN KEY (message_id) REFERENCES messages(uuid) ON DELETE CASCADE,
                                     CONSTRAINT fk_attachment_binary FOREIGN KEY (attachment_id) REFERENCES binary_contents(uuid) ON DELETE CASCADE
);
