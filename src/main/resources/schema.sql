-- DROP TABLE IF EXISTS message_attachments;
-- DROP TABLE IF EXISTS messages;
-- DROP TABLE IF EXISTS read_statuses;
-- DROP TABLE IF EXISTS user_statuses;
-- DROP TABLE IF EXISTS users;
-- DROP TABLE IF EXISTS channels;
-- DROP TABLE IF EXISTS binary_contents;

CREATE TABLE IF NOT EXISTS binary_contents
(
    id           UUID PRIMARY KEY,
    created_at   TIMESTAMPTZ  NOT NULL,
    file_name    VARCHAR(255),
    size         BIGINT       NOT NULL,
    content_type varchar(100) NOT NULL,
    bytes        BYTEA        NOT NULL
);

CREATE TABLE IF NOT EXISTS users
(
    id         UUID PRIMARY KEY,
    created_at TIMESTAMPTZ         NOT NULL,
    updated_at TIMESTAMPTZ,
    username   VARCHAR(50) UNIQUE  NOT NULL,
    email      VARCHAR(100) UNIQUE NOT NULL,
    password   VARCHAR(60)         NOT NULL,
    profile_id UUID,
    constraint fk_users_binary_contents FOREIGN KEY (profile_id) REFERENCES binary_contents (id)
);

CREATE TABLE IF NOT EXISTS user_statuses
(
    id             UUID PRIMARY KEY,
    created_at     TIMESTAMPTZ NOT NULL,
    updated_at     TIMESTAMPTZ,
    user_id        UUID UNIQUE,
    last_active_at TIMESTAMPTZ NOT NULL,
    constraint fk_user_status_users FOREIGN KEY (user_id) REFERENCES users (id) ON DELETE CASCADE
);

CREATE TABLE IF NOT EXISTS channels
(
    id          UUID PRIMARY KEY,
    created_at  TIMESTAMPTZ                                        NOT NULL,
    updated_at  TIMESTAMPTZ,
    name        VARCHAR(100),
    description VARCHAR(500),
    type        VARCHAR(10) CHECK ( type IN ('PUBLIC', 'PRIVATE')) NOT NULL
);

CREATE TABLE IF NOT EXISTS read_statuses
(
    id           UUID PRIMARY KEY,
    created_at   TIMESTAMPTZ NOT NULL,
    update_at    TIMESTAMPTZ,
    user_id      UUID,
    channel_id   UUID,
    last_read_at TIMESTAMPTZ NOT NULL,
    CONSTRAINT fk_read_status_users FOREIGN KEY (user_id) REFERENCES users (id) ON DELETE CASCADE,
    CONSTRAINT fk_read_status_channels FOREIGN KEY (channel_id) REFERENCES channels (id) ON DELETE CASCADE
);

CREATE TABLE IF NOT EXISTS messages
(
    id         UUID PRIMARY KEY,
    created_at TIMESTAMPTZ NOT NULL,
    updated_at TIMESTAMPTZ,
    content    TEXT,
    channel_id UUID,
    author_id  UUID,
    CONSTRAINT fk_messages_channels FOREIGN KEY (channel_id) REFERENCES channels (id) ON DELETE CASCADE,
    CONSTRAINT fk_messages_users FOREIGN KEY (author_id) REFERENCES users (id) ON DELETE SET NULL
);

CREATE TABLE IF NOT EXISTS message_attachments
(
    message_id    UUID,
    attachment_id UUID,
    CONSTRAINT fk_message_attachments_messages FOREIGN KEY (message_id) REFERENCES messages (id) ON DELETE CASCADE,
    CONSTRAINT fk_message_attachments_binary_contents FOREIGN KEY (attachment_id) REFERENCES binary_contents (id) ON DELETE CASCADE
);