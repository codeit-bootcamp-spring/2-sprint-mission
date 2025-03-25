package com.sprint.mission.discodeit.dto.request;


import lombok.Builder;
import lombok.Getter;

@Getter
public class UserUpdateRequest {
    private String newUsername;
    private String newEmail;
    private String newPassword;

    @Builder
    public UserUpdateRequest(String newUsername, String newEmail, String newPassword) {
        this.newUsername = newUsername;
        this.newEmail = newEmail;
        this.newPassword = newPassword;
    }
}
