package com.sprint.mission.discodeit.core.user.usecase.dto;

import com.sprint.mission.discodeit.core.user.entity.User;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Builder;

import java.time.Instant;
import java.util.UUID;

@Builder
@Schema(description = "User Item")
public record UserResult(
    @Schema(description = "User Id", example = "3fa85f64-5717-4562-b3fc-2c963f66afa6")
    UUID id,

    @Schema(description = "User 생성 시각", example = "2025-04-03T01:38:38.006Z")
    Instant createdAt,

    @Schema(description = "User 수정 시각", example = "2025-04-03T01:38:38.006Z")
    Instant updatedAt,

    @Schema(description = "User 이름", example = "string")
    String username,

    @Schema(description = "User 이메일", example = "string")
    String email,

    @Schema(description = "User 프로필 Id", example = "3fa85f64-5717-4562-b3fc-2c963f66afa6")
    UUID profileId,

    @Schema(description = "User 온라인 상태", example = "true")
    boolean online
) {

  public static UserResult create(User user, boolean online) {
    return UserResult.builder()
        .id(user.getId())
        .profileId(user.getProfileId())
        .username(user.getName())
        .email(user.getEmail())
        .createdAt(user.getCreatedAt())
        .updatedAt(user.getUpdatedAt())
        .online(online).build();
  }
}
