
CREATE TABLE IF NOT EXISTS binary_contents(
    id uuid PRIMARY KEY ,
    created_at timestamp with time zone NOT NULL,
    file_name VARCHAR(255) NOT NULL ,
    size BIGINT NOT NULL ,
    content_type VARCHAR(100) NOT NULL
);

CREATE TABLE IF NOT EXISTS users(
    id uuid PRIMARY KEY,
    created_at timestamp with time zone NOT NULL,
    updated_at timestamp with time zone,
    username VARCHAR(50) UNIQUE NOT NULL,
    email VARCHAR(100) UNIQUE NOT NULL,
    password VARCHAR(60) NOT NULL,
    profile_id uuid,
    CONSTRAINT fk_profile_id
        FOREIGN KEY (profile_id)
            REFERENCES binary_contents (id)
            ON DELETE SET NULL
);


CREATE TABLE IF NOT EXISTS channels(
    id uuid PRIMARY KEY ,
    created_at timestamp with time zone NOT NULL,
    updated_at timestamp with time zone,
    name VARCHAR(100) ,
    description VARCHAR(500) ,
    type VARCHAR(10) NOT NULL,
    CONSTRAINT chk_channel_type
        CHECK (type IN ('PUBLIC', 'PRIVATE'))

);

CREATE TABLE IF NOT EXISTS messages(
    id uuid PRIMARY KEY ,
    created_at timestamp with time zone NOT NULL,
    updated_at timestamp with time zone,
    content text ,
    channel_id uuid NOT NULL ,
    author_id uuid,
    CONSTRAINT fk_channel_id
        FOREIGN KEY (channel_id)
            REFERENCES channels(id)
            ON DELETE CASCADE ,
    CONSTRAINT fk_author_id
        FOREIGN KEY (author_id)
            REFERENCES users(id)
            ON DELETE SET NULL
);

CREATE TABLE IF NOT EXISTS user_statuses(
    id uuid PRIMARY KEY ,
    created_at timestamp with time zone NOT NULL,
    updated_at timestamp with time zone,
    user_id uuid UNIQUE,
    last_active_at timestamptz NOT NULL ,
    CONSTRAINT fk_user_id
        FOREIGN KEY (user_id)
            REFERENCES users (id)
            ON DELETE CASCADE
);

CREATE TABLE IF NOT EXISTS read_statuses(
    id uuid PRIMARY KEY ,
    created_at timestamp with time zone NOT NULL,
    updated_at timestamp with time zone,
    user_id uuid ,
    channel_id uuid ,
    last_read_at timestamptz NOT NULL ,
    CONSTRAINT fk_user_id
        FOREIGN KEY (user_id)
            REFERENCES users(id)
            ON DELETE CASCADE,
    CONSTRAINT fk_channel_id
        FOREIGN KEY (channel_id)
            REFERENCES channels(id)
            ON DELETE CASCADE,
    CONSTRAINT uk_user_id_channel_id
        UNIQUE (user_id, channel_id)
);

CREATE TABLE IF NOT EXISTS message_attachments(
    message_id uuid,
    attachment_id uuid,
    CONSTRAINT fk_message_id
        FOREIGN KEY (message_id)
            REFERENCES messages(id)
            ON DELETE CASCADE ,
    CONSTRAINT fk_attachment_id
        FOREIGN KEY (attachment_id)
            REFERENCES binary_contents(id)
            ON DELETE CASCADE
);


