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
    boolean online
) {

  public static UserStatusUpdateResponse fromEntity(UserStatus userStatus) {
    return new UserStatusUpdateResponse(
        userStatus.getId(),
        userStatus.getCreatedAt(),
        userStatus.getUpdatedAt(),
        userStatus.getUser(),
        userStatus.getLastActiveAt(),
        userStatus.isOnline()
    );
  }
}
