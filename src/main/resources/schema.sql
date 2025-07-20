DROP TABLE IF EXISTS jwt_sessions;
DROP TABLE IF EXISTS async_task_failures;
DROP TABLE IF EXISTS message_attachments;
DROP TABLE IF EXISTS messages;
DROP TABLE IF EXISTS notifications;
DROP TABLE IF EXISTS read_statuses;
DROP TABLE IF EXISTS channels;
DROP TABLE IF EXISTS users;
DROP TABLE IF EXISTS binary_contents;

-- 테이블
-- User
CREATE TABLE users
(
    id         uuid PRIMARY KEY,
    created_at timestamp with time zone NOT NULL,
    updated_at timestamp with time zone,
    username   varchar(50) UNIQUE       NOT NULL,
    email      varchar(100) UNIQUE      NOT NULL,
    password   varchar(60)              NOT NULL,
    role       varchar(30),
    profile_id uuid
);

--JwtSession
CREATE TABLE jwt_sessions
(
    id            uuid PRIMARY KEY,
    created_at    timestamp with time zone NOT NULL,
    updated_at    timestamp with time zone,

    user_id       uuid                     NOT NULL,
    access_token  TEXT UNIQUE              NOT NULL,
    refresh_token TEXT UNIQUE              NOT NULL,
    expires_at    timestamp with time zone NOT NULL
);


-- AsyncTaskFailure
CREATE TABLE async_task_failures
(
    id             uuid PRIMARY KEY,
    created_at     timestamp with time zone NOT NULL,
    task_name      varchar(255)             NOT NULL,
    request_id     varchar(255)             NOT NULL,
    failure_reason varchar(1000)            NOT NULL
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

-- Notification
CREATE TABLE notifications
(
    id         uuid PRIMARY KEY,
    created_at timestamp with time zone NOT NULL,
    updated_at timestamp with time zone,
    user_id    uuid                     NOT NULL,
    title      varchar(255)             NOT NULL,
    content    varchar(255)             NOT NULL,
    type       varchar(20)              NOT NULL,
    target_id  uuid
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
    id           uuid PRIMARY KEY,
    created_at   timestamp with time zone NOT NULL,
    updated_at   timestamp with time zone,
    user_id      uuid                     NOT NULL,
    channel_id   uuid                     NOT NULL,
    last_read_at timestamp with time zone NOT NULL,
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

-- Notification (N) -> User (1)
ALTER TABLE notifications
    ADD CONSTRAINT fk_notification_user
        FOREIGN KEY (user_id)
            REFERENCES users (id)
            ON DELETE CASCADE;