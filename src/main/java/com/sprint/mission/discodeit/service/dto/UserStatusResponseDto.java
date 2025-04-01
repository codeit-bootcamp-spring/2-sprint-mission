package com.sprint.mission.discodeit.service.dto;

import com.sprint.mission.discodeit.entity.BinaryContent;
import com.sprint.mission.discodeit.entity.UserStatus;

import java.time.Instant;
import java.util.UUID;

public record UserStatusResponseDto(
        UUID id,
        UUID userId,
        Instant lastActiveAt
) {
    public static UserStatusResponseDto convertToResponseDto(UserStatus userStatus) {
        return new UserStatusResponseDto(userStatus.getId(), userStatus.getUserId(), userStatus.getLastActiveAt());
    }

}
