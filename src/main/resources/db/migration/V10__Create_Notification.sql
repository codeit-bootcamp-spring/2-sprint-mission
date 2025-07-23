CREATE TABLE notification
(
    id                UUID PRIMARY KEY,
    created_at        timestamp with time zone NOT NULL,
    updated_at        timestamp with time zone,
    receiver_id       UUID                     NOT NULL,
    title             varchar(258)             NOT NULL,
    content           varchar(1024)            NOT NULL,
    notification_type varchar(258)             NOT NULL,
    target_id         UUID
);