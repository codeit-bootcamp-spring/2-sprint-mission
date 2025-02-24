package com.sprint.mission.discodeit.entity;

public class User extends BaseEntity {
    private String username;

    public User(String username) {
        super();
        this.username = username;
    }

    public String getUsername() {
        return username;
    }

    public void updateUsername(String newUsername) {
        this.username = newUsername;
        update();
    }

}
