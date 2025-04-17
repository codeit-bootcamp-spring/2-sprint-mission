package com.sprint.mission.discodeit.dto.service.user.userstatus;

import com.sprint.mission.discodeit.entity.UserStatus;
import java.time.Instant;
import java.util.UUID;
import lombok.Builder;

@Builder
public record UserStatusUpdateDto(
    UUID id,
    UUID userId,
    Instant lastActiveAt
) {

  public static UserStatusUpdateDto of(UserStatus status) {
    return UserStatusUpdateDto.builder()
        .id(status.getId())
        .userId(status.getUser().getId())
        .lastActiveAt(status.getLastActiveAt())
        .build();
  }
}
