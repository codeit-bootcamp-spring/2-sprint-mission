package com.sprint.mission.discodeit.dto.user;

import java.util.UUID;

public record UserUpdateDto(
        String newUsername,
        String newEmail,
        String newPassword,
        UUID newProfileId
) {
}
