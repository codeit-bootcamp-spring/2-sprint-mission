package com.sprint.mission.discodeit.dto.service;

import java.util.UUID;

public record CreateUserParam(
        String username,
        String email,
        String password,
        UUID profileId
) {
}
