package com.sprint.mission.discodeit.dto;

import java.util.UUID;

public record ReadUserDto(
        UUID userKey,
        String userName,
        String email,
        boolean isOnline
) {
}
