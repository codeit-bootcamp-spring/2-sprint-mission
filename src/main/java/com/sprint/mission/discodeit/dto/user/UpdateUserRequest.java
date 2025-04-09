package com.sprint.mission.discodeit.dto.user;

import lombok.Getter;

@Getter
public class UpdateUserRequest {

    String newUsername;
    String newEmail;
    String newPassword;

    public UpdateUserRequest(String newUsername, String newEmail, String newPassword) {
        this.newUsername = newUsername;
        this.newEmail = newEmail;
        this.newPassword = newPassword;
    }
}
