package com.sprint.mission.discodeit.UpdateRequests;

import lombok.Getter;

@Getter
public class UserUpdateRequest {
    private String newUserName;
    private String newEmail;
    private String newPassword;
}
