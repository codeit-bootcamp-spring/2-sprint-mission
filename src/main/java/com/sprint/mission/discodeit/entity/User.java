package com.sprint.mission.discodeit.entity;

import java.util.Date;
import java.util.UUID;

public class User extends Entity {
    private String username;
    private String password;
    private String email;

    public User(UUID uuid, String username, String password, String email) {
        super();
        this.username = username;
        this.password = password;
        this.email = email;

    }

    public String getUsername() { return username; }
    public void updateUsername(String username) {
        this.username = username;
        this.updatedAt = System.currentTimeMillis();
    }
    public void setPassword(String password) {
        this.password = password;
        this.updatedAt = System.currentTimeMillis();
    }
    public String getPassword() { return password; }
    public String getEmail() { return email; }
    public void setEmail(String email) {
        this.email = email;
        this.updatedAt = System.currentTimeMillis();
    }
}