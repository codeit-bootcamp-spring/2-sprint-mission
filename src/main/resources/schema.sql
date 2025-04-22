CREATE TABLE binary_contents(
                                id UUID PRIMARY KEY,
                                created_at timestamptz NOT NULL,
                                file_name VARCHAR(255) NOT NULL,
                                size BIGINT NOT NULL ,
                                content_type VARCHAR(100) NOT NULL
);

CREATE TABLE users(
                      id UUID PRIMARY KEY ,
                      created_at timestamptz NOT NULL ,
                      updated_at timestamptz ,
                      username VARCHAR(50) UNIQUE NOT NULL ,
                      email VARCHAR(100) UNIQUE NOT NULL ,
                      password VARCHAR(60) NOT NULL ,
                      profile_id UUID ,
                      CONSTRAINT fk_profile FOREIGN KEY (profile_id)
                          REFERENCES binary_contents(id)
                          ON DELETE SET NULL
);

CREATE TABLE user_statuses(
                              id UUID PRIMARY KEY ,
                              created_at timestamptz NOT NULL ,
                              updated_at timestamptz,
                              user_id UUID UNIQUE NOT NULL ,
                              last_active_at timestamptz NOT NULL ,
                              CONSTRAINT fk_user_status FOREIGN KEY (user_id)
                                  REFERENCES users(id)
                                  ON DELETE CASCADE
);

CREATE TABLE channels(
                         id UUID PRIMARY KEY ,
                         created_at timestamptz NOT NULL ,
                         updated_at timestamptz,
                         name VARCHAR(100),
                         description VARCHAR(500),
                         type VARCHAR(10) NOT NULL CHECK (type IN ('PUBLIC', 'PRIVATE'))
);

CREATE TABLE messages(
                         id UUID PRIMARY KEY ,
                         created_at timestamptz NOT NULL ,
                         updated_at timestamptz,
                         content TEXT,
                         channel_id UUID NOT NULL,
                         author_id UUID,
                         CONSTRAINT fk_messages_channel_id FOREIGN KEY (channel_id)
                             REFERENCES channels(id)
                             ON DELETE CASCADE ,
                         CONSTRAINT fk_messages_author_id FOREIGN KEY (author_id)
                             REFERENCES users(id)
                             ON DELETE SET NULL
);

CREATE TABLE read_statuses(
                              id UUID PRIMARY KEY ,
                              created_at timestamptz NOT NULL ,
                              updated_at timestamptz,
                              user_id UUID NOT NULL ,
                              channel_id UUID NOT NULL,
                              last_read_at timestamptz NOT NULL ,
                              CONSTRAINT uq_user_channel UNIQUE (user_id,channel_id),
                              CONSTRAINT fk_read_user FOREIGN KEY (user_id)
                                  REFERENCES users(id)
                                  ON DELETE CASCADE,
                              CONSTRAINT fk_read_channel FOREIGN KEY (channel_id)
                                  REFERENCES channels(id)
                                  ON DELETE CASCADE
);

CREATE TABLE message_attachments(
                                    message_id UUID,
                                    attachment_id UUID,
                                    PRIMARY KEY (message_id, attachment_id),
                                    CONSTRAINT fk_message_id FOREIGN KEY (message_id)
                                        REFERENCES messages(id)
                                        ON DELETE CASCADE ,
                                    CONSTRAINT fk_message_attachment FOREIGN KEY (attachment_id)
                                        REFERENCES binary_contents(id)
                                        ON DELETE CASCADE
);
