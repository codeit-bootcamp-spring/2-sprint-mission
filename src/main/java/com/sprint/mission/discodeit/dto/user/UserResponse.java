package com.sprint.mission.discodeit.dto.user;

import com.sprint.mission.discodeit.entity.user.User;

import java.time.Instant;
import java.util.UUID;

public record UserResponse(
    UUID id,
    Instant createdAt,
    Instant updatedAt,
    String username,
    String email,
    UUID profileId,
    boolean online
) {

  public static UserResponse fromEntity(User user, boolean online) {
    return new UserResponse(
        user.getId(),
        user.getCreatedAt(),
        user.getUpdatedAt(),
        user.getUsername(),
        user.getEmail(),
        user.getProfileId(),
        online
    );
  }
}
