package com.sprint.mission.discodeit.dto;

import java.util.UUID;

public record UpdateUserResponseDto(
        String userName,
        String pwd,
        String email,
        UUID profileKey
) {
}
