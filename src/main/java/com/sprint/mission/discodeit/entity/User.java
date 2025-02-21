package com.sprint.mission.discodeit.entity;

public class User extends BaseEntity {
    private String userName;

    public String getUserName() {
        return userName;
    }

    public String getUsername() {
        return userName;
    }

    public void updateUsername(String userName) {
        this.userName = userName;
    }
}
