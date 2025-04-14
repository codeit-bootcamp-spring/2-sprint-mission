package com.sprint.mission.discodeit.adapter.inbound.user.response;

import com.sprint.mission.discodeit.core.user.entity.User;
import io.swagger.v3.oas.annotations.media.Schema;
import java.time.Instant;
import java.util.UUID;
import lombok.Builder;

@Builder
@Schema(description = "User 성공적으로 생성됨")
public record UserCreateResponse(
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
    @Schema(description = "User 비밀번호", example = "string")
    String password,
    @Schema(description = "User 프로필 Id", example = "3fa85f64-5717-4562-b3fc-2c963f66afa6")
    UUID profileId
) {

  public static UserCreateResponse create(User user) {
    return UserCreateResponse.builder()
        .id(user.getId())
        .createdAt(user.getCreatedAt())
        .updatedAt(user.getUpdatedAt())
        .username(user.getName())
        .email(user.getEmail())
        .password(user.getPassword())
        .profileId(user.getProfile().getId()).build();
  }
}
