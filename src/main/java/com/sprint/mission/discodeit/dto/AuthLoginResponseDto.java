package com.sprint.mission.discodeit.dto;

import java.util.UUID;

public record AuthLoginResponseDto(
        UUID userKey,
        String userName,
        String email
) {
}
