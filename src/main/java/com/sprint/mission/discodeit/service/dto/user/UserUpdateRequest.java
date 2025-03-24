package com.sprint.mission.discodeit.service.dto.user;

import java.util.Optional;
import java.util.UUID;

public record UserUpdateRequest(
        UUID id,
        Optional<String> newUsername,
        Optional<String> newEemail,
        Optional<String> newPassword
) {
}

