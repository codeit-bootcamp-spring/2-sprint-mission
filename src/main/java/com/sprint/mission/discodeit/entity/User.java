package com.sprint.mission.discodeit.entity;

import lombok.Getter;

import java.io.Serializable;
import java.util.UUID;

@Getter
public class User extends BaseEntity implements Serializable {
    private static final long serialVersionUID = 1L;

    private String userName;
    private String userEmail;
    private String userPassword;
    private UUID profileId;

    public User(String userName, String userEmail, String userPassword, UUID profileId) {
        super();
        this.userName = userName;
        this.userEmail = userEmail;
        this.userPassword = userPassword;
        this.profileId = profileId;
    }

    public void update(String newUserName, String newUserEmail, String newUserPassword, UUID newProfileId) {
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
        if (newProfileId != null && !newProfileId.equals(this.profileId)) {
            this.profileId = newProfileId;
            anyValueUpdated = true;
        }
        if (anyValueUpdated) {
            this.update();
        }
    }
}