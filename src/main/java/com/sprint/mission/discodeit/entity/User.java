package com.sprint.mission.discodeit.entity;

import lombok.Getter;
import lombok.Setter;

import java.io.Serializable;
import java.time.Instant;
import java.util.UUID;

@Getter
@Setter
public class User implements Serializable {
    private static final long serialVersionUID = 1L;

    protected final UUID id;
    protected Instant createAt;
    protected Instant updateAt;

    private String userName;
    private String email;
    private UserStatus status;
    private BinaryContent profileImage;

    public User() {
        this.id = UUID.randomUUID();
        this.createAt = Instant.now();
        this.updateAt = createAt;
        this.userName = "Unknown";
    }

    public User(String userName, String email, UserStatus status) {
        this();
        this.userName = userName;
        this.email = email;
        this.status = status;
    }

    public User(UUID id, String userName, String email, Instant createAt, Instant updateAt, UserStatus status) {
        this.id = id;
        this.userName = userName;
        this.email = email;
        this.createAt = createAt;
        this.updateAt = updateAt;
        this.status = status;
    }

    public User(String userName) {
        this();
        this.userName = userName;
    }

    public void updateUsername(String newUserName) {
        this.userName = newUserName;
    }

    public long getCreateAtMillis() {
        return createAt.toEpochMilli();
    }

    public void setCreateAtMillis(long createAtMillis) {
        this.createAt = Instant.ofEpochMilli(createAtMillis);
    }

    public long getUpdateAtMillis() {
        return updateAt.toEpochMilli();
    }

    public void setUpdateAtMillis(long updateAtMillis) {
        this.updateAt = Instant.ofEpochMilli(updateAtMillis);
    }
}
