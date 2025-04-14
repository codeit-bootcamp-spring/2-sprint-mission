package com.sprint.mission.discodeit.dto.data;

import com.sprint.mission.discodeit.entity.User;

import java.time.Instant;
import java.util.UUID;

public record UserDto(
    UUID id,
    Instant createdAt,
    Instant updatedAt,
    String username,
    String email,
    UUID profileId,
    Boolean online
) {

  public static UserDto from(User user) {
    return new UserDto(
        user.getId(),
        user.getCreatedAt(),
        user.getUpdatedAt(),
        user.getUsername(),
        user.getEmail(),
        // null-safe profile id
        user.getProfile() != null ? user.getProfile().getId() : null,
        // null-safe online flag
        user.getStatus() != null && user.getStatus().isOnline()
    );
  }
}