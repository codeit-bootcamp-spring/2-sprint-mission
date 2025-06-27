package com.sprint.mission.discodeit.core.user.dto.response;

import com.sprint.mission.discodeit.core.user.entity.User;
import com.sprint.mission.discodeit.core.user.entity.UserStatus;
import java.time.Instant;
import java.util.UUID;
import lombok.Builder;

@Builder
public record UserStatusDto(
    UUID id,
    UUID userId,
    Instant lastActiveAt
) {

  public static UserStatusDto create(User user, UserStatus status) {
    return UserStatusDto.builder()
        .id(status.getId())
        .userId(user.getId())
        .lastActiveAt(status.getLastActiveAt()).build();
  }
}
