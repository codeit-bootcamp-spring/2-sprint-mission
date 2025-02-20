package com.sprint.mission.discodeit.entity;

public class User extends BaseEntity {
    private String userName;
    private String userEmail;
    private String password;

    public User(String userName, String userEmail, String password) {
        super();
        this.userName = userName;
        this.userEmail = userEmail;
        this.password = password;

    }

    public String getUserName() {
        return userName;
    }

    public String getUserEmail() {
        return userEmail;
    }

    public String getPassWord() {
        return password;
    }

}
