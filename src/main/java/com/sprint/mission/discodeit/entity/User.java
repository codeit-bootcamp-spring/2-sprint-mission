package com.sprint.mission.discodeit.entity;


import java.util.UUID;

public class User extends BaseEntity {
    private String username;
    private String email;

    public User(UUID id, long createdAt, String username, String email) {
        super(id, createdAt);
        this.username = username;
        this.email = email;
    }

    public String getUsername() {
        return username;
    }

    public String getEmail() {
        return email;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public void setEmail(String email) {
        this.email = email;
    }

}
