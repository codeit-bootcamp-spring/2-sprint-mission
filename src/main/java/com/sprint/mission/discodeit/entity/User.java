package com.sprint.mission.discodeit.entity;


import java.io.Serializable;
import java.util.UUID;

public class User extends BaseEntity implements Serializable {
    private static final long serialVersionUID = 1L;
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

    @Override
    public String toString() {
        return "User{" +
                super.toString() +
                "username='" + username + '\'' +
                ", email='" + email + '\'' +
                '}';
    }
}
