-- 📌 users
CREATE TABLE users (
    id UUID PRIMARY KEY,
    created_at TIMESTAMPTZ NOT NULL,
    updated_at TIMESTAMPTZ NOT NULL,
    username VARCHAR(50) UNIQUE NOT NULL,
    email VARCHAR(100) UNIQUE NOT NULL,
    password VARCHAR(60) NOT NULL,
    profile_id UUID,
    CONSTRAINT fk_users_profile FOREIGN KEY (profile_id)
        REFERENCES binary_contents(id) ON DELETE SET NULL
);

-- 📌 binary_contents
CREATE TABLE binary_contents (
    id UUID PRIMARY KEY,
    created_at TIMESTAMPTZ NOT NULL,
    file_name VARCHAR(255) NOT NULL,
    size BIGINT NOT NULL,
    content_type VARCHAR(100) NOT NULL,
    bytes BYTEA NOT NULL
);

-- 📌 user_statuses
CREATE TABLE user_statuses (
    id UUID PRIMARY KEY,
    created_at TIMESTAMPTZ NOT NULL,
    updated_at TIMESTAMPTZ NOT NULL,
    user_id UUID NOT NULL UNIQUE,
    last_active_at TIMESTAMPTZ,
    CONSTRAINT fk_user_status_user FOREIGN KEY (user_id)
        REFERENCES users(id) ON DELETE CASCADE
);

-- 📌 channels
CREATE TABLE channels (
    id UUID PRIMARY KEY,
    created_at TIMESTAMPTZ NOT NULL,
    updated_at TIMESTAMPTZ NOT NULL,
    name VARCHAR(100),
    description VARCHAR(500),
    type VARCHAR(10) NOT NULL CHECK (type IN ('PUBLIC', 'PRIVATE'))
);

-- 📌 messages
CREATE TABLE messages (
    id UUID PRIMARY KEY,
    created_at TIMESTAMPTZ NOT NULL,
    updated_at TIMESTAMPTZ NOT NULL,
    content TEXT,
    channel_id UUID,
    author_id UUID,
    CONSTRAINT fk_message_channel FOREIGN KEY (channel_id)
        REFERENCES channels(id) ON DELETE CASCADE,
    CONSTRAINT fk_message_author FOREIGN KEY (author_id)
        REFERENCES users(id) ON DELETE SET NULL
);

-- 📌 read_statuses
CREATE TABLE read_statuses (
    id UUID PRIMARY KEY,
    created_at TIMESTAMPTZ NOT NULL,
    updated_at TIMESTAMPTZ NOT NULL,
    user_id UUID NOT NULL,
    channel_id UUID NOT NULL,
    last_read_at TIMESTAMPTZ,
    CONSTRAINT fk_read_status_user FOREIGN KEY (user_id)
        REFERENCES users(id) ON DELETE CASCADE,
    CONSTRAINT fk_read_status_channel FOREIGN KEY (channel_id)
        REFERENCES channels(id) ON DELETE CASCADE,
    CONSTRAINT uk_read_status UNIQUE (user_id, channel_id)
);

-- 📌 message_attachments
CREATE TABLE message_attachments (
    message_id UUID NOT NULL,
    attachment_id UUID NOT NULL,
    CONSTRAINT fk_message_attachment_message FOREIGN KEY (message_id)
        REFERENCES messages(id) ON DELETE CASCADE,
    CONSTRAINT fk_message_attachment_file FOREIGN KEY (attachment_id)
        REFERENCES binary_contents(id) ON DELETE CASCADE
);