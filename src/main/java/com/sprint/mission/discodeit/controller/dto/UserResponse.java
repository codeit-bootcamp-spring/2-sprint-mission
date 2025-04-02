package com.sprint.mission.discodeit.controller.dto;

import com.sprint.mission.discodeit.entity.User;
import java.time.Instant;
import java.util.UUID;

public record UserResponse(
    UUID id,
    Instant createdAt,
    Instant updatedAt,
    String username,
    String email,
    String password,
    UUID profileId
) {

  public static UserResponse of(User user) {
    return new UserResponse(
        user.getId(), user.getCreatedAt(), user.getUpdatedAt(),
        user.getUsername(), user.getEmail(), user.getPassword(), user.getProfileId()
    );
  }
}
