create table binary_contents(
    id uuid primary key,
    created_at timestamp not null,
    file_name varchar(255) not null,
    size bigint not null,
    content_type varchar(100) not null,
    bytes bytea not null
);

create table users(
    id uuid primary key,
    created_at timestamp default current_timestamp NOT NULL,
    updated_at timestamp,
    username varchar(50) unique not null,
    email varchar(100) unique not null,
    password varchar(60) not null,
    profile_id uuid,
    constraint fk_binary_contents
              foreign key (profile_id)
                  references binary_contents(id)
                  on delete set null
);

create table user_status(
    id uuid primary key,
    created_at timestamp default current_timestamp,
    updated_at timestamp,
    last_active_at timestamp not null,
    user_id uuid unique not null,
    constraint fk_user
                        foreign key (user_id)
                        references users(id)
                        on delete cascade
);

create table channels(
    id uuid primary key,
    created_at timestamp default current_timestamp not null,
    updated_at timestamp,
    name varchar(100),
    description varchar(500),
    type varchar(10) not null
);

create table messages(
    id uuid primary key,
    created_at timestamp default current_timestamp not null,
    updated_at timestamp,
    content text,
    channel_id uuid not null,
    author_id uuid,
    constraint fk_user
        foreign key (author_id)
            references users(id)
            on delete set null ,
    constraint fk_channel
        foreign key (channel_id)
            references channels(id)
            on delete cascade
);

create table read_statuses(
    id uuid primary key,
    created_at timestamp default current_timestamp not null,
    updated_at timestamp,
    last_read_at timestamp not null,
    user_id uuid unique,
    channel_id uuid unique,
    constraint fk_user
        foreign key (user_id)
            references users(id)
            on delete cascade,
    constraint fk_channel
        foreign key (channel_id)
            references channels(id)
            on delete cascade
);

create table message_attachments(
    message_id uuid,
    attachment_id uuid,
    constraint fk_message
        foreign key (message_id)
            references messages(id)
            on delete cascade,
    constraint fk_binary_content
        foreign key (attachment_id)
            references binary_contents(id)
            on delete cascade
);