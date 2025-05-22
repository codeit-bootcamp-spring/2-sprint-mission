CREATE TABLE binary_contents
(
    id           UUID PRIMARY KEY,
    created_at   timestamp with time zone NOT NULL,
    file_name    VARCHAR(255)             NOT NULL,
    size         BIGINT                   NOT NULL,
    content_type VARCHAR(100)             NOT NULL
);

CREATE TABLE users
(
    id         UUID PRIMARY KEY,
    created_at timestamp with time zone NOT NULL,
    updated_at timestamp with time zone,
    username   VARCHAR(50)              NOT NULL UNIQUE,
    email      VARCHAR(100)             NOT NULL UNIQUE,
    password   VARCHAR(60)              NOT NULL,
    profile_id UUID,
    CONSTRAINT fk_users_binary_contents
        FOREIGN KEY (profile_id)
            REFERENCES binary_contents (id)
            ON DELETE SET NULL
);

CREATE TABLE channels
(
    id          UUID PRIMARY KEY,
    created_at  timestamp with time zone NOT NULL,
    updated_at  timestamp with time zone,
    name        VARCHAR(100),
    description VARCHAR(500),
    type        VARCHAR(10)              NOT NULL CHECK (type IN ('PUBLIC', 'PRIVATE'))
);

CREATE TABLE messages
(
    id         UUID PRIMARY KEY,
    created_at timestamp with time zone NOT NULL,
    updated_at timestamp with time zone,
    content    TEXT,
    channel_id UUID                     NOT NULL,
    author_id  UUID,
    CONSTRAINT fk_messages_channels
        FOREIGN KEY (channel_id)
            REFERENCES channels (id)
            ON DELETE CASCADE,
    CONSTRAINT fk_messages_users
        FOREIGN KEY (author_id)
            REFERENCES users (id)
            ON DELETE SET NULL
);

CREATE TABLE user_statuses
(
    id             UUID PRIMARY KEY,
    created_at     timestamp with time zone NOT NULL,
    updated_at     timestamp with time zone,
    user_id        UUID UNIQUE              NOT NULL,
    last_active_at timestamp with time zone NOT NULL,
    CONSTRAINT fk_user_statuses_users
        FOREIGN KEY (user_id)
            REFERENCES users (id)
            ON DELETE CASCADE
);

CREATE TABLE read_statuses
(
    id           UUID PRIMARY KEY,
    created_at   timestamp with time zone NOT NULL,
    updated_at   timestamp with time zone,
    user_id      UUID,
    channel_id   UUID,
    last_read_at timestamp with time zone NOT NULL,
    CONSTRAINT unique_users_channels
        UNIQUE (user_id, channel_id),
    CONSTRAINT fk_read_statuses_users
        FOREIGN KEY (user_id)
            REFERENCES users (id)
            ON DELETE CASCADE,
    CONSTRAINT fk_read_statuses_channels
        FOREIGN KEY (channel_id)
            REFERENCES channels (id)
            ON DELETE CASCADE
);

CREATE TABLE message_attachments
(
    message_id    UUID,
    attachment_id UUID,
    Constraint pk_messages_attachments
        PRIMARY KEY (message_id, attachment_id),
    CONSTRAINT fk_messages
        FOREIGN KEY (message_id)
            REFERENCES messages (id)
            ON DELETE CASCADE,
    CONSTRAINT fk_binary_contents
        FOREIGN KEY (attachment_id)
            REFERENCES binary_contents (id)
            ON DELETE CASCADE
);