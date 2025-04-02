package com.sprint.mission.discodeit.core.user.usecase.dto;

import com.sprint.mission.discodeit.core.user.entity.User;
import lombok.Builder;

import java.time.Instant;
import java.util.UUID;

@Builder
public record UserResult(
    UUID id,
    UUID profileId,
    String name,
    String email,
    Instant createdAt,
    Instant updatedAt,
    boolean online
) {

  public static UserResult create(User user, boolean online) {
    return UserResult.builder()
        .id(user.getId())
        .profileId(user.getProfileId())
        .name(user.getName())
        .email(user.getEmail())
        .createdAt(user.getCreatedAt())
        .updatedAt(user.getUpdatedAt())
        .online(online).build();
  }
}
