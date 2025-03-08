package com.sprint.mission.discodeit.entity;



public class User extends MainDomain {
    private String userName;
    private String email;
    private String password;


    public User(String userName, String email, String password){
        super();
        this.userName = userName;
        this.email = email;
        this.password = password;
    }

    public String getUserName() {
        return userName;
    }

    public String getEmail() {
        return email;
    }

    public String getPassword() {
        return password;
    }


    public void updateUser(String newUserName, String newEmail, String newPassword){
        boolean anyValueUpdated = false;
        if(newUserName != null && !newUserName.equals(this.userName)){
            this.userName = newUserName;
            anyValueUpdated = true;
        }
        if(newEmail != null && !newEmail.equals(this.email)){
            this.email = newEmail;
            anyValueUpdated = true;
        }
        if(newPassword != null && !newPassword.equals(this.password)){
            this.password = newPassword;
            anyValueUpdated = true;
        }
        if(anyValueUpdated){
            update();
        }
    }

    @Override
    public String toString() {
        return "User {" +
                "userID= " + getId()  +
                ", userName= " + userName +
                ", email= " + email +
                ", password= " + password + "}";
    }
}
