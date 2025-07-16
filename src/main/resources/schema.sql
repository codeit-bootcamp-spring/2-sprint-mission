-- DROP TABLE IF EXISTS message_attachments;
-- DROP TABLE IF EXISTS messages;
-- DROP TABLE IF EXISTS read_statuses;
-- DROP TABLE IF EXISTS user_statuses;
-- DROP TABLE IF EXISTS users;
-- DROP TABLE IF EXISTS channels;
-- DROP TABLE IF EXISTS binary_contents;
-- DROP TABLE IF EXISTS persistent_logins;
-- DROP TABLE IF EXISTS jwt_sessions;

CREATE TABLE IF NOT EXISTS jwt_sessions
(
    id            uuid PRIMARY KEY,
    user_id       uuid                     NOT NULL,
    access_token  TEXT UNIQUE              NOT NULL,
    refresh_token TEXT UNIQUE              NOT NULL,
    expires_at    timestamp with time zone NOT NULL,
    created_at    timestamp with time zone NOT NULL,
    updated_at    timestamp with time zone
);

CREATE TABLE IF NOT EXISTS persistent_logins
(
    username  VARCHAR(64) NOT NULL,
    series    VARCHAR(64) PRIMARY KEY,
    token     VARCHAR(64) NOT NULL,
    last_used TIMESTAMP   NOT NULL
);

CREATE TABLE IF NOT EXISTS binary_contents
(
    id            UUID PRIMARY KEY,
    created_at    TIMESTAMP WITH TIME ZONE NOT NULL,
    updated_at    TIMESTAMP WITH TIME ZONE,
    file_name     VARCHAR(255),
    size          BIGINT                   NOT NULL,
    content_type  varchar(100)             NOT NULL,
    upload_status VARCHAR(20) CHECK ( upload_status IN ('WAITING', 'SUCCESS', 'FAILED')) DEFAULT 'WAITING'
);

CREATE TABLE IF NOT EXISTS users
(
    id         UUID PRIMARY KEY,
    created_at TIMESTAMP WITH TIME ZONE                                          NOT NULL,
    updated_at TIMESTAMP WITH TIME ZONE,
    username   VARCHAR(50) UNIQUE                                                NOT NULL,
    email      VARCHAR(100) UNIQUE                                               NOT NULL,
    password   VARCHAR(60)                                                       NOT NULL,
    profile_id UUID,
    role       VARCHAR(20) CHECK ( role IN ('ADMIN', 'CHANNEL_MANAGER', 'USER')) NOT NULL DEFAULT 'USER',
    constraint fk_users_binary_contents FOREIGN KEY (profile_id) REFERENCES binary_contents (id)
);

CREATE TABLE IF NOT EXISTS channels
(
    id          UUID PRIMARY KEY,
    created_at  TIMESTAMP WITH TIME ZONE                           NOT NULL,
    updated_at  TIMESTAMP WITH TIME ZONE,
    name        VARCHAR(100),
    description VARCHAR(500),
    type        VARCHAR(10) CHECK ( type IN ('PUBLIC', 'PRIVATE')) NOT NULL
);

CREATE TABLE IF NOT EXISTS read_statuses
(
    id           UUID PRIMARY KEY,
    created_at   TIMESTAMP WITH TIME ZONE NOT NULL,
    updated_at   TIMESTAMP WITH TIME ZONE,
    user_id      UUID,
    channel_id   UUID,
    last_read_at TIMESTAMP WITH TIME ZONE NOT NULL,
    CONSTRAINT fk_read_status_users FOREIGN KEY (user_id) REFERENCES users (id) ON DELETE CASCADE,
    CONSTRAINT fk_read_status_channels FOREIGN KEY (channel_id) REFERENCES channels (id) ON DELETE CASCADE
);

CREATE TABLE IF NOT EXISTS messages
(
    id         UUID PRIMARY KEY,
    created_at TIMESTAMP WITH TIME ZONE NOT NULL,
    updated_at TIMESTAMP WITH TIME ZONE,
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