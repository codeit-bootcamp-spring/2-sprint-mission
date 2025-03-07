package com.sprint.mission.discodeit.entity;

public class User extends CommonEntity {
    private String username;
    public User(String username) {
        super();
        this.username = username;

    }
    public String getUsername() {
        return username;
    }

    @Override
    public String toString() {
        return "[ username: "+getUsername()+" USER "+super.toString();
    }
}
