package com.sprint.mission.discodeit.dto;

import com.sprint.mission.discodeit.entity.UserStatus;
import java.time.Instant;
import java.util.UUID;

public record UserStatusResponseDto(
    UUID id,
    Instant createdAt,
    Instant updatedAt,
    UUID userId,
    Instant lastActiveAt,
    boolean online
) {

  public static UserStatusResponseDto convertToResponseDto(UserStatus userStatus) {
    return new UserStatusResponseDto(userStatus.getId(), userStatus.getCreatedAt(),
        userStatus.getUpdatedAt(), userStatus.getUserId(),
        userStatus.getLastActiveAt(), userStatus.isOnline());
  }

}
