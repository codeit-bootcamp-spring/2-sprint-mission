package com.sprint.mission.discodeit.service.dto.user;

import java.util.UUID;

public record UserUpdateRequest(
        UUID id,
        String newUsername,
        String newEmail,
        String newPassword
) {
}

