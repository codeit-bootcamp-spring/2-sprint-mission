package com.sprint.mission.discodeit.entity;


import lombok.Getter;

import java.util.UUID;

@Getter
public class User extends MainDomain {
    private String userName;
    private String email;
    private String password;
    private UUID profileId;

    public User(String userName, String email, String password){
        super();
        this.userName = userName;
        this.email = email;
        this.password = password;
    }

    public void useProfileId(UUID profileId) {
        this.profileId = profileId;
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
                ", email= " + email + "}";
    }
}
