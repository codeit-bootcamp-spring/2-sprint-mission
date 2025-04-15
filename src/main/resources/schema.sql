-- binary_contents
CREATE TABLE binary_contents
(
    id           uuid,
    created_at   timestamptz  NOT NULL,
    filename     varchar(255) NOT NULL,
    size         bigint       NOT NULL,
    content_type varchar(100) NOT NULL,
    bytes        bytea        NOT NULL,

    CONSTRAINT pk_binary_contents PRIMARY KEY (id)
);

-- users
CREATE TABLE users
(
    id         uuid,
    created_at timestamptz  NOT NULL,
    updated_at timestamptz,
    username   varchar(50)  NOT NULL,
    email      varchar(100) NOT NULL,
    password   varchar(60)  NOT NULL,
    profile_id uuid,

    CONSTRAINT pk_users_id PRIMARY KEY (id),
    CONSTRAINT uq_users_username UNIQUE (username),
    CONSTRAINT uq_users_email UNIQUE (email),
    CONSTRAINT fk_profile FOREIGN KEY (profile_id)
        REFERENCES binary_contents (id)
        ON DELETE SET NULL
);


-- channels
CREATE TABLE channels
(
    id          uuid,
    created_at  timestamptz NOT NULL,
    updated_at  timestamptz,
    name        varchar(100),
    description varchar(500),
    type        varchar(10) NOT NULL,

    CONSTRAINT pk_channels_id PRIMARY KEY (id),
    CONSTRAINT ck_channels_type CHECK ( type IN ('PUBLIC', 'PRIVATE') )
);

-- messages
CREATE TABLE messages
(
    id         uuid PRIMARY KEY,
    created_at timestamptz NOT NULL,
    updated_at timestamptz,
    text       text,
    channel_id uuid        NOT NULL,
    author_id  uuid,

    CONSTRAINT pk_messages_id PRIMARY KEY (id),
    CONSTRAINT fk_messages_channel_id FOREIGN KEY (channel_id)
        REFERENCES channels (id)
        ON DELETE CASCADE,
    CONSTRAINT fk_messages_user_id FOREIGN KEY (author_id)
        REFERENCES users (id)
        ON DELETE SET NULL
);

-- user_statuses
CREATE TABLE user_statuses
(
    id             uuid,
    created_at     timestamptz NOT NULL,
    updated_at     timestamptz,
    user_id        uuid        NOT NULL,
    last_active_at timestamptz NOT NULL,

    CONSTRAINT pk_user_statuses_id PRIMARY KEY (id),
    CONSTRAINT fk_user_statuses_user_id FOREIGN KEY (user_id)
        REFERENCES users (id)
        ON DELETE CASCADE,
    CONSTRAINT uq_user_statuses_user_id UNIQUE (user_id)
);


-- read_statuses
CREATE TABLE read_statuses
(
    id           uuid,
    created_at   timestamptz NOT NULL,
    updated_at   timestamptz,
    user_id      uuid,
    channel_id   uuid,
    last_read_at timestamptz NOT NULL,

    CONSTRAINT pk_read_statuses_id PRIMARY KEY (id),
    CONSTRAINT fk_read_statuses_user_id FOREIGN KEY (user_id)
        REFERENCES users (id)
        ON DELETE CASCADE,
    CONSTRAINT fk_user_statuses_channel_id FOREIGN KEY (channel_id)
        REFERENCES channels (id)
        ON DELETE CASCADE,
    CONSTRAINT uq_user_statuses_user_id_channel_id UNIQUE (user_id, channel_id)
);

-- message_attachments
CREATE TABLE message_attachments
(
    message_id    uuid,
    attachment_id uuid,

    CONSTRAINT fk_message_attachments_message_id FOREIGN KEY (message_id)
        REFERENCES messages (id)
        ON DELETE CASCADE,
    CONSTRAINT fk_message_attachments_attachments_id FOREIGN KEY (attachment_id)
        REFERENCES binary_contents (id)
        ON DELETE CASCADE
);
