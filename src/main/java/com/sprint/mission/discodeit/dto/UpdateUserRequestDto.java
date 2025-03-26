package com.sprint.mission.discodeit.dto;

import java.util.UUID;

public record UpdateUserRequestDto(
        UUID userKey,
        String userName,
        String pwd,
        String email,
        UUID profileKey
) {
}
