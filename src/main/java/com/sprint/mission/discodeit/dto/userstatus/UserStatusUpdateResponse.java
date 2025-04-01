package com.sprint.mission.discodeit.dto.userstatus;

import com.sprint.mission.discodeit.entity.user.UserStatus;
import java.time.Instant;
import java.util.UUID;

public record UserStatusUpdateResponse(
    UUID id,
    Instant createdAt,
    Instant updatedAt,
    UUID userId,
    Instant lastActiveAt,
    boolean isOnline
) {

  public static UserStatusUpdateResponse fromEntity(UserStatus userStatus) {
    return new UserStatusUpdateResponse(
        userStatus.getId(),
        userStatus.getCreatedAt(),
        userStatus.getUpdatedAt(),
        userStatus.getUserId(),
        userStatus.getLastActiveAt(),
        userStatus.isOnline()
    );
  }
}
