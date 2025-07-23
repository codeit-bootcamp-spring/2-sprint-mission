CREATE TABLE async_failure
(
    id           UUID PRIMARY KEY,
    task_name    varchar(128)             not null,
    request_id   varchar(128)             not null,
    failure_type varchar(128)             not null,
    created_at   timestamp with time zone NOT NULL,
    updated_at   timestamp with time zone
);
