package com.sprint.mission.discodeit.controller.dto;

import com.sprint.mission.discodeit.entity.UserStatus;
import java.time.Instant;
import java.util.UUID;

public record UserStatusUpdateResponse(
    UUID id,
    Instant createdAt,
    Instant updatedAt,
    UUID userId,
    Instant lastActiveAt,
    boolean online
) {

  public static UserStatusUpdateResponse of(UserStatus status) {
    return new UserStatusUpdateResponse(
        status.getId(), status.getCreatedAt(), status.getUpdatedAt(), status.getUserId(),
        status.getLastActiveAt(), status.isOnline()
    );
  }
}
