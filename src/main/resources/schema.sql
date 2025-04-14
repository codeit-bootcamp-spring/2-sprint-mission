create table binary_contents
(
    id           uuid         not null
        primary key,
    created_at   timestamp    not null,
    file_name    varchar(225) not null,
    size         bigint       not null,
    content_type varchar(100) not null,
    bytes        bytea        not null
);

alter table binary_contents
    owner to discodeit_user;

create table users
(
    id         uuid         not null
        primary key,
    created_at timestamp    not null,
    updated_at timestamp,
    username   varchar(50)  not null
        unique,
    email      varchar(100) not null
        unique,
    password   varchar(60)  not null,
    profile_id uuid
                            references binary_contents
                                on delete set null
);

alter table users
    owner to discodeit_user;

create table user_statuses
(
    id             uuid      not null
        constraint user_status_pkey
            primary key,
    created_at     timestamp not null,
    updated_at     timestamp,
    user_id        uuid      not null
        constraint user_status_user_id_key
            unique
        constraint user_status_user_id_fkey
            references users
            on delete cascade,
    last_active_at timestamp not null
);

alter table user_statuses
    owner to discodeit_user;

create table channels
(
    id          uuid                     not null
        primary key,
    created_at  timestamp with time zone not null,
    updated_at  timestamp with time zone not null,
    name        varchar(100)             not null,
    description varchar(500),
    type        varchar(10)              not null
        constraint channels_type_check
            check ((type)::text = ANY
                   ((ARRAY ['PUBLIC'::character varying, 'PRIVATE'::character varying])::text[]))
);

alter table channels
    owner to discodeit_user;

create table messages
(
    id         uuid                     not null
        primary key,
    created_at timestamp with time zone not null,
    updated_at timestamp with time zone not null,
    content    text,
    channel_id uuid                     not null
        references channels
            on delete cascade,
    author_id  uuid
                                        references users
                                            on delete set null
);

alter table messages
    owner to discodeit_user;

create table message_attachments
(
    message_id    uuid not null
        references messages
            on delete cascade,
    attachment_id uuid not null
        references binary_contents
            on delete cascade,
    primary key (message_id, attachment_id)
);

alter table message_attachments
    owner to discodeit_user;

create table read_statuses
(
    id           uuid                     not null
        primary key,
    created_at   timestamp with time zone not null,
    updated_at   timestamp with time zone not null,
    user_id      uuid                     not null
        references users
            on delete cascade,
    channel_id   uuid                     not null
        references channels
            on delete cascade,
    last_read_at timestamp with time zone not null,
    unique (user_id, channel_id)
);

alter table read_statuses
    owner to discodeit_user;


