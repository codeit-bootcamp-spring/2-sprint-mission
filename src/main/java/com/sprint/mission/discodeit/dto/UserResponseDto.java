package com.sprint.mission.discodeit.dto;

import com.sprint.mission.discodeit.entity.User;
import java.time.Instant;
import java.util.UUID;

public record UserResponseDto(
    UUID id,
    Instant createdAt,
    Instant updatedAt,
    String username,
    String email,
    UUID profileId,
    boolean online
) {

  public static UserResponseDto convertToResponseDto(User user, boolean online) {
    return new UserResponseDto(user.getId(), user.getCreatedAt(), user.getUpdatedAt(),
        user.getUsername(), user.getEmail(), user.getProfileId(), online);
  }

}
