create table binary_contents
(
    id           UUID         not null
        primary key,
    created_at   timestamp    not null,
    file_name    VARCHAR(225) not null,
    size         BIGINT       not null,
    content_type VARCHAR(100) not null
);

create table users
(
    id         UUID         not null
        primary key,
    created_at timestamp    not null,
    updated_at timestamp,
    username   VARCHAR(50)  not null
        unique,
    email      VARCHAR(100) not null
        unique,
    password   VARCHAR(60)  not null,
    profile_id UUID
                            references binary_contents
                                on delete set null
);

create table channels
(
    id          UUID         not null
        primary key,
    created_at  TIMESTAMP    not null,
    updated_at  TIMESTAMP,
    name        VARCHAR(100) not null,
    description VARCHAR(500),
    type        VARCHAR(10)  not null
        constraint channels_type_check
            check (type in ('PUBLIC', 'PRIVATE'))
);

create table messages
(
    id         UUID      not null
        primary key,
    created_at TIMESTAMP not null,
    updated_at TIMESTAMP,
    content    CLOB,
    channel_id UUID      not null
        references channels
            on delete cascade,
    author_id  UUID
                         references users
                             on delete set null
);

create table message_attachments
(
    message_id    UUID not null
        references messages
            on delete cascade,
    attachment_id UUID not null
        references binary_contents
            on delete cascade,
    primary key (message_id, attachment_id)
);

create table read_statuses
(
    id           UUID      not null
        primary key,
    created_at   TIMESTAMP not null,
    updated_at   TIMESTAMP,
    user_id      UUID      not null
        references users
            on delete cascade,
    channel_id   UUID      not null
        references channels
            on delete cascade,
    last_read_at TIMESTAMP not null,
    unique (user_id, channel_id)
);

create table user_statuses
(
    id             UUID      not null
        primary key,
    created_at     TIMESTAMP not null,
    updated_at     TIMESTAMP,
    user_id        UUID      not null
        unique
        references users
            on delete cascade,
    last_active_at TIMESTAMP not null
);
