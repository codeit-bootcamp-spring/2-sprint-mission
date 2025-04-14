package com.sprint.mission.discodeit.controller.dto;

import com.sprint.mission.discodeit.entity.UserStatus;
import java.time.Instant;
import java.util.UUID;
import lombok.Builder;

@Builder
public record UserStatusUpdateResponse(
    UUID id,
    UUID userId,
    Instant lastActiveAt
) {

  public static UserStatusUpdateResponse of(UserStatus status) {
    return UserStatusUpdateResponse.builder()
        .id(status.getId())
        .userId(status.getUserId())
        .lastActiveAt(status.getLastActiveAt())
        .build();
  }
}
