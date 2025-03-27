package com.sprint.mission.discodeit.dto.request;

import java.util.UUID;

public record UserUpdateRequest(
        UUID userKey,
        String newUsername,
        String newEmail,
        String newPassword,
        UUID newProfileKey
) {
}
