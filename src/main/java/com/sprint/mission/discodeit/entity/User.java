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

    public String getUserEmail() {
        return userEmail;
    }

    public String getUserPassword() {
        return userPassword;
    }

    public void update(String newUserName, String newUserEmail, String newUserPassword) {
        boolean anyValueUpdated = false;
        if (newUserName != null && !newUserName.equals(this.userName)) {
            this.userName = newUserName;
            anyValueUpdated = true;
        }
        if (newUserEmail != null && !newUserEmail.equals(this.userEmail)) {
            this.userEmail = newUserEmail;
            anyValueUpdated = true;
        }
        if (newUserPassword != null && !newUserPassword.equals(this.userPassword)) {
            this.userPassword = newUserPassword;
            anyValueUpdated = true;
        }
        if (anyValueUpdated) {
            this.update();
        }
    }
}