package com.sprint.mission.discodeit.dto.auth;

import com.sprint.mission.discodeit.entity.user.User;
import java.time.Instant;
import java.util.UUID;

public record AuthLoginDto(
    UUID id,
    Instant createdAt,
    Instant updatedAt,
    String username,
    String email,
    UUID profileId
) {

  public static AuthLoginDto fromEntity(User user) {
    return new AuthLoginDto(
        user.getId(),
        user.getCreatedAt(),
        user.getUpdatedAt(),
        user.getUsername(),
        user.getEmail(),
        user.getProfile().getId()
    );
  }
}