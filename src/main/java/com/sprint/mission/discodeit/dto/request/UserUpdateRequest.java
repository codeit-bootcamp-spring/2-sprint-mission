package com.sprint.mission.discodeit.dto.request;

import java.util.Optional;

public record UserUpdateRequest(
    Optional<String> newUsername,
    Optional<String> newEmail,
    Optional<String> newPassword
) {

}
