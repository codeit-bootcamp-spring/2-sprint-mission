package com.sprint.mission.discodeit.entity;

public class User extends BaseEntity {
    private String userName;

    public User (String userName) {
        this.userName = userName;
    }

    public String getUsername() {
        return userName;
    }

    public void updateUsername(String userName) {
        this.userName = userName;
    }
}
