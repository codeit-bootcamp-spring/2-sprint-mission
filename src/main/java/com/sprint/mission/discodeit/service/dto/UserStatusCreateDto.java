package com.sprint.mission.discodeit.service.dto;

import com.sprint.mission.discodeit.entity.UserStatus;

import java.time.Instant;
import java.util.UUID;

public record UserStatusCreateDto(
        UUID userId,
        Instant lastActiveAt
) {
    public UserStatus convertCreateDtoToUserStatus() {
        return new UserStatus(userId);
    }
}
