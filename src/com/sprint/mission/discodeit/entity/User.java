package com.sprint.mission.discodeit.entity;

public class User extends Entity {
    private String username;

    public User(String username) {
        super();
        this.username = username;
    }

    public String getUsername() { return username; }
    public void updateUsername(String username) {
        this.username = username;
        this.updatedAt = System.currentTimeMillis();
    }
}