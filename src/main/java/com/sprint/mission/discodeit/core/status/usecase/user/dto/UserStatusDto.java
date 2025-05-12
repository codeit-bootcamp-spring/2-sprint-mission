package com.sprint.mission.discodeit.core.status.usecase.user.dto;

import com.sprint.mission.discodeit.core.status.entity.UserStatus;
import java.time.Instant;
import java.util.UUID;
import lombok.Builder;

@Builder
public record UserStatusDto(
    UUID id,
    UUID userId,
    Instant lastActiveAt
) {

  public static UserStatusDto create(UserStatus status) {
    return UserStatusDto.builder()
        .id(status.getId())
        .userId(status.getUser().getId())
        .lastActiveAt(status.getLastActiveAt()).build();
  }
}
