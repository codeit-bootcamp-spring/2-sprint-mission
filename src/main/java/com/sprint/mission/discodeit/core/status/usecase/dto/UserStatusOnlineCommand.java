package com.sprint.mission.discodeit.core.status.usecase.dto;

import com.sprint.mission.discodeit.core.user.controller.dto.UserStatusRequest;
import io.swagger.v3.oas.annotations.media.Schema;
import java.time.Instant;
import java.util.UUID;
import lombok.Builder;

@Builder
public record UserStatusOnlineCommand(
    UUID userId,
    Instant lastActiveAt
) {

  public static UserStatusOnlineCommand create(UUID userId, UserStatusRequest requestBody) {
    return UserStatusOnlineCommand.builder()
        .userId(userId)
        .lastActiveAt(requestBody.newLastActiveAt()).build();
  }

}
