package com.sprint.mission.discodeit.dto;

import java.util.UUID;

public record UserCreateRequestDto(
        String username,
        String email,
        String password,
        UUID profileId
) {
}
