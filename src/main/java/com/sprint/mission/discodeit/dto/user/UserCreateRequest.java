package com.sprint.mission.discodeit.dto.user;

import java.util.UUID;

public record UserCreateRequest(
        String userName,
        String userEmail,
        String password,
        UUID profileId
) {
}
