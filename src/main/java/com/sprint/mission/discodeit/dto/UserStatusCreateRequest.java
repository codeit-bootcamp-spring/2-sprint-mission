package com.sprint.mission.discodeit.dto;

import com.sprint.mission.discodeit.entity.UserStatus;
import java.time.Instant;
import java.util.UUID;

public record UserStatusCreateRequest(
    UUID userId,
    Instant lastActiveAt
) {

  public UserStatus convertCreateRequestToUserStatus() {
    return new UserStatus(userId, lastActiveAt);
  }

}
