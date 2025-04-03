package com.sprint.mission.discodeit.dto.auth;

import com.sprint.mission.discodeit.entity.user.User;
import java.time.Instant;
import java.util.UUID;

public record AuthLoginResponse(
    UUID id,
    Instant createdAt,
    Instant updatedAt,
    String username,
    String email,
    UUID profileId
) {

  public static AuthLoginResponse fromEntity(User user) {
    return new AuthLoginResponse(
        user.getId(),
        user.getCreatedAt(),
        user.getUpdatedAt(),
        user.getUsername(),
        user.getEmail(),
        user.getProfileId()
    );
  }
}