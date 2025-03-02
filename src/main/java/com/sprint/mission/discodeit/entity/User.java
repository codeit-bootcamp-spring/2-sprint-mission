package com.sprint.mission.discodeit.entity;

import java.util.Objects;

public class User extends MainDomain {
    private String userName;

    public User(String userName){
        super();
        this.userName = userName;
    }

    public String getUserName() {
        return userName;
    }

    public void updateUser(String userName){
        this.userName = userName;
        update();
    }

    @Override
    public String toString() {
        return "User{" +
                "userName='" + userName + '\'' +
                '}';
    }
}
