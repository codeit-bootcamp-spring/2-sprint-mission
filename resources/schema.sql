CREATE TABLE binary_contents
(
    id           UUID PRIMARY KEY,
    created_at   TIMESTAMP    NOT NULL,
    file_name    VARCHAR(255) NOT NULL,
    size         BIGINT       NOT NULL,
    content_type VARCHAR(100) NOT NULL
    --btye 삭제
);


CREATE TABLE users
(
    id         UUID PRIMARY KEY,
    created_at TIMESTAMP    NOT NULL,
    updated_at TIMESTAMP,
    username   VARCHAR(50)  NOT NULL UNIQUE,
    email      VARCHAR(100) NOT NULL UNIQUE,
    password   VARCHAR(60)  NOT NULL,
    profile_id UUID,
    FOREIGN KEY (profile_id) REFERENCES binary_contents (id) ON DELETE SET NULL
);

CREATE TABLE channels
(
    id          UUID PRIMARY KEY,
    created_at  TIMESTAMP   NOT NULL,
    updated_at  TIMESTAMP,
    name        VARCHAR(100),
    description VARCHAR(50),
    type        VARCHAR(10) NOT NULL CHECK (type IN ('DM', 'PUBLIC', 'PRIVATE'))
);


CREATE TABLE messages
(
    id         UUID PRIMARY KEY,
    created_at TIMESTAMP NOT NULL,
    updated_at TIMESTAMP,
    text       TEXT,
    channel_id UUID      NOT NULL,
    author_id  UUID,
    FOREIGN KEY (channel_id) REFERENCES channels (id) ON DELETE CASCADE,
    FOREIGN KEY (author_id) REFERENCES users (id) ON DELETE SET NULL
);


CREATE TABLE user_statuses
(
    id             UUID PRIMARY KEY,
    created_at     TIMESTAMP NOT NULL,
    updated_at     TIMESTAMP,
    user_id        UUID      NOT NULL UNIQUE,
    last_active_at TIMESTAMP NOT NULL,
    FOREIGN KEY (user_id) REFERENCES users (id) ON DELETE CASCADE
);


CREATE TABLE read_statuses
(
    id           UUID PRIMARY KEY,
    created_at   TIMESTAMP NOT NULL,
    updated_at   TIMESTAMP,
    user_id      UUID      NOT NULL,
    channel_id   UUID      NOT NULL,
    last_read_at TIMESTAMP NOT NULL,
    UNIQUE (user_id, channel_id),
    FOREIGN KEY (user_id) REFERENCES users (id) ON DELETE CASCADE,
    FOREIGN KEY (channel_id) REFERENCES channels (id) ON DELETE CASCADE
);


CREATE TABLE message_attachments
(
    message_id    UUID NOT NULL,
    attachment_id UUID NOT NULL,
    FOREIGN KEY (message_id) REFERENCES messages (id) ON DELETE CASCADE,
    FOREIGN KEY (attachment_id) REFERENCES binary_contents (id) ON DELETE CASCADE
);
