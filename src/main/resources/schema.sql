-- 테이블
-- User
CREATE TABLE users
(
    id         uuid PRIMARY KEY,
    created_at timestamp with time zone NOT NULL,
    updated_at timestamp with time zone,
    username   varchar(50) UNIQUE       NOT NULL,
    role       varchar(20)              NOT NULL,
    email      varchar(100) UNIQUE      NOT NULL,
    password   varchar(60)              NOT NULL,
    profile_id uuid
);

-- jwt
CREATE TABLE jwt_sessions
(
    id                       UUID PRIMARY KEY,
    user_id                  UUID         NOT NULL,
    access_token             VARCHAR(500) NOT NULL,
    refresh_token            VARCHAR(500) NOT NULL,
    issued_at                TIMESTAMP    NOT NULL,
    refresh_token_expires_at TIMESTAMP,
    CONSTRAINT fk_jwt_sessions_user FOREIGN KEY (user_id) REFERENCES users (id) ON DELETE CASCADE
);

-- async_failure
CREATE TABLE async_task_failure
(
    id             uuid PRIMARY KEY,
    created_at     timestamp with time zone NOT NULL,
    task_name      varchar(255)             NOT NULL,
    request_id     varchar(255)             NOT NULL,
    failure_reason varchar(500)             NOT NULL
);

-- BinaryContent
CREATE TABLE binary_contents
(
    id            uuid PRIMARY KEY,
    created_at    timestamp with time zone NOT NULL,
    file_name     varchar(255)             NOT NULL,
    size          bigint                   NOT NULL,
    content_type  varchar(100)             NOT NULL,
    upload_status varchar(100)
--     ,bytes        bytea        NOT NULL
);

-- UserStatus
CREATE TABLE user_statuses
(
    id             uuid PRIMARY KEY,
    created_at     timestamp with time zone NOT NULL,
    updated_at     timestamp with time zone,
    user_id        uuid UNIQUE              NOT NULL,
    last_active_at timestamp with time zone NOT NULL
);

-- Channel
CREATE TABLE channels
(
    id          uuid PRIMARY KEY,
    created_at  timestamp with time zone NOT NULL,
    updated_at  timestamp with time zone,
    name        varchar(100),
    description varchar(500),
    type        varchar(10)              NOT NULL
);

-- Notification
CREATE TABLE notifications
(
    id          uuid PRIMARY KEY,
    created_at  timestamp with time zone NOT NULL,
    title       varchar(100)             NOT NULL,
    content     varchar(500)             NOT NULL,
    type        varchar(20)              NOT NULL,
    target_id   uuid,
    receiver_id uuid                     NOT NULL,
    CONSTRAINT fk_notifications_receiver
        FOREIGN KEY (receiver_id) REFERENCES users (id)
            ON DELETE CASCADE
);

-- Message
CREATE TABLE messages
(
    id         uuid PRIMARY KEY,
    created_at timestamp with time zone NOT NULL,
    updated_at timestamp with time zone,
    content    text,
    channel_id uuid                     NOT NULL,
    author_id  uuid
);

-- Message.attachments
CREATE TABLE message_attachments
(
    message_id    uuid,
    attachment_id uuid,
    PRIMARY KEY (message_id, attachment_id)
);

-- ReadStatus
CREATE TABLE read_statuses
(
    id                   uuid PRIMARY KEY,
    created_at           timestamp with time zone NOT NULL,
    updated_at           timestamp with time zone,
    user_id              uuid                     NOT NULL,
    channel_id           uuid                     NOT NULL,
    last_read_at         timestamp with time zone NOT NULL,
    notification_enabled boolean,
    UNIQUE (user_id, channel_id)
);


-- 제약 조건
-- User (1) -> BinaryContent (1)
ALTER TABLE users
    ADD CONSTRAINT fk_user_binary_content
        FOREIGN KEY (profile_id)
            REFERENCES binary_contents (id)
            ON DELETE SET NULL;

-- UserStatus (1) -> User (1)
ALTER TABLE user_statuses
    ADD CONSTRAINT fk_user_status_user
        FOREIGN KEY (user_id)
            REFERENCES users (id)
            ON DELETE CASCADE;

-- Message (N) -> Channel (1)
ALTER TABLE messages
    ADD CONSTRAINT fk_message_channel
        FOREIGN KEY (channel_id)
            REFERENCES channels (id)
            ON DELETE CASCADE;

-- Message (N) -> Author (1)
ALTER TABLE messages
    ADD CONSTRAINT fk_message_user
        FOREIGN KEY (author_id)
            REFERENCES users (id)
            ON DELETE SET NULL;

-- MessageAttachment (1) -> BinaryContent (1)
ALTER TABLE message_attachments
    ADD CONSTRAINT fk_message_attachment_binary_content
        FOREIGN KEY (attachment_id)
            REFERENCES binary_contents (id)
            ON DELETE CASCADE;

-- ReadStatus (N) -> User (1)
ALTER TABLE read_statuses
    ADD CONSTRAINT fk_read_status_user
        FOREIGN KEY (user_id)
            REFERENCES users (id)
            ON DELETE CASCADE;

-- ReadStatus (N) -> User (1)
ALTER TABLE read_statuses
    ADD CONSTRAINT fk_read_status_channel
        FOREIGN KEY (channel_id)
            REFERENCES channels (id)
            ON DELETE CASCADE;