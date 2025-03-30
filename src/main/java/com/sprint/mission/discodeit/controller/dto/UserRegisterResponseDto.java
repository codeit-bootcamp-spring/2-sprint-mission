package com.sprint.mission.discodeit.controller.dto;

import java.time.Instant;
import java.util.UUID;

public record UserRegisterResponseDto(
        UUID userId,
        String email,
        String nickname,
        Instant createdAt
) {
}
