package com.sprint.mission.discodeit.entity;

public class User extends BaseEntity {
    private String userName;
    private String userEmail;
    private String userPassword;

    public User(String userName, String userEmail, String userPassword) {
        super();
        this.userName = userName;
        this.userEmail = userEmail;
        this.userPassword = userPassword;
    }

    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
        update();
    }

    public String getUserEmail() {
        return userEmail;
    }

    public void setUserEmail(String userEmail) {
        this.userEmail = userEmail;
        update();
    }

    public String getUserPassword() {
        return userPassword;
    }

    public void setUserPassword(String userPassword) {
        this.userPassword = userPassword;
        update();
    }
}
