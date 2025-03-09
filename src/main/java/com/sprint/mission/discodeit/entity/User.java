package com.sprint.mission.discodeit.entity;

import java.io.Serializable;

public class User extends CommonEntity implements Serializable {
    private static final long serialVersionUID = 1L;
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
