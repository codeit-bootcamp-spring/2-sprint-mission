CREATE TABLE users
(
    id         UUID PRIMARY KEY,
    created_at TIMESTAMPTZ  NOT NULL,
    updated_at TIMESTAMPTZ,
    username   VARCHAR(50)  NOT NULL UNIQUE,
    email      VARCHAR(100) NOT NULL UNIQUE,
    password   VARCHAR(60)  NOT NULL,
    profile_id UUID,
    CONSTRAINT fk_binary_contents_users
        FOREIGN KEY (profile_id)
            REFERENCES binary_contents (id)
            ON DELETE SET NULL
);

CREATE TABLE binary_contents
(
    id           UUID PRIMARY KEY,
    created_at   TIMESTAMPTZ  NOT NULL,
    file_name    VARCHAR(255) NOT NULL,
    size         BIGINT       NOT NULL,
    content_type VARCHAR(100) NOT NULL
);

CREATE TABLE user_statuses
(
    id             UUID PRIMARY KEY,
    created_at     TIMESTAMPTZ NOT NULL,
    updated_at     TIMESTAMPTZ,
    user_id        UUID        NOT NULL,
    last_active_at TIMESTAMPTZ NOT NULL,
    CONSTRAINT fk_users_user_statuses
        FOREIGN KEY (user_id)
            REFERENCES users (id)
            ON DELETE CASCADE
);

CREATE TABLE channels
(
    id          UUID PRIMARY KEY,
    created_at  TIMESTAMPTZ NOT NULL,
    updated_at  TIMESTAMPTZ,
    name        VARCHAR(100),
    description VARCHAR(500),
    type        VARCHAR(20) NOT NULL
);

CREATE TABLE read_statuses
(
    id           UUID PRIMARY KEY,
    created_at   TIMESTAMPTZ NOT NULL,
    updated_at   TIMESTAMPTZ,
    user_id      UUID        NOT NULL,
    channel_id   UUID        NOT NULL,
    last_read_at TIMESTAMPTZ NOT NULL,
    CONSTRAINT fk_users_read_statuses
        FOREIGN KEY (user_id)
            REFERENCES users (id)
            ON DELETE CASCADE,
    CONSTRAINT fk_channels_read_statuses
        FOREIGN KEY (channel_id)
            REFERENCES channels (id)
            ON DELETE CASCADE,
    CONSTRAINT uq_user_channel UNIQUE (user_id, channel_id)
);

CREATE TABLE messages
(
    id         UUID PRIMARY KEY,
    created_at TIMESTAMPTZ NOT NULL,
    updated_at TIMESTAMPTZ,
    content    TEXT,
    channel_id UUID        NOT NULL,
    author_id  UUID,
    CONSTRAINT fk_channels_messages
        FOREIGN KEY (channel_id)
            REFERENCES channels (id)
            ON DELETE CASCADE,
    CONSTRAINT fk_users_messages
        FOREIGN KEY (author_id)
            REFERENCES users (id)
            ON DELETE SET NULL
);

CREATE TABLE message_attachments
(
    message_id    UUID,
    attachment_id UUID,
    PRIMARY KEY (message_id, attachment_id),
    CONSTRAINT fk_messages_message_attachments
        FOREIGN KEY (message_id)
            REFERENCES messages (id)
            ON DELETE CASCADE,
    CONSTRAINT fk_binary_contents_message_attachments
        FOREIGN KEY (attachment_id)
            REFERENCES binary_contents (id)
            ON DELETE CASCADE
);