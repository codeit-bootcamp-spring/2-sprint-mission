package com.sprint.mission.discodeit.service.dto.user;

import com.sprint.mission.discodeit.controller.dto.UserUpdateDataRequest;
import java.util.UUID;

public record UserUpdateRequest(
        UUID id,
        String newUsername,
        String newEmail,
        String newPassword
) {
    public static UserUpdateRequest of(UUID id, UserUpdateDataRequest dataRequest) {
        String username = dataRequest.newUsername();
        String email = dataRequest.newEmail();
        String password = dataRequest.newPassword();
        return new UserUpdateRequest(id, username, email, password);
    }
}

