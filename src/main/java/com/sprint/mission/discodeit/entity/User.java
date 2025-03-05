package com.sprint.mission.discodeit.entity;

import java.io.Serializable;
import java.util.UUID;

public class User implements Serializable {
    private static final long serialVersionUID = 1L;
    private String userName;
    protected UUID id;
    protected Long createAt;
    protected Long updateAt;

    public User() {
        this.id = UUID.randomUUID();
        this.createAt = System.currentTimeMillis();
        this.updateAt = createAt;
        this.userName = "Unknown";
    }

    public UUID getId() {
        return id;
    }

    public void setId(UUID id) {
        this.id = id;
    }

    public Long getCreateAt() {
        return createAt;
    }

    public Long getUpdateAt() {
        return updateAt;
    }


    public User (String userName) {
        this();
        this.userName = userName;
    }

    public String getUsername() {
        return userName;
    }

    public void updateUsername(String newUserName) {
        this.userName = newUserName;
    }
}
