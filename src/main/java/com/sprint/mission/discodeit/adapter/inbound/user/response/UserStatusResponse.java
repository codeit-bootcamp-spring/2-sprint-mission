package com.sprint.mission.discodeit.adapter.inbound.user.response;

import com.sprint.mission.discodeit.core.status.entity.UserStatus;
import io.swagger.v3.oas.annotations.media.Schema;
import java.time.Instant;
import java.util.UUID;
import lombok.Builder;

@Builder
@Schema(description = "User Status Response")
public record UserStatusResponse(
    @Schema(description = "User Status Id", example = "3fa85f64-5717-4562-b3fc-2c963f66afa6")
    UUID id,

    @Schema(description = "User Status 생성 시각", example = "2025-04-03T01:38:38.006Z")
    Instant createdAt,

    @Schema(description = "User Status 수정 시각", example = "2025-04-03T01:38:38.006Z")
    Instant updatedAt,

    @Schema(description = "User Id", example = "3fa85f64-5717-4562-b3fc-2c963f66afa6")
    UUID userId,

    @Schema(description = "User Status 활성 시각", example = "2025-04-03T01:38:38.006Z")
    Instant lastActiveAt,

    @Schema(description = "User 온라인 상태", example = "true")
    boolean online
) {

  public static UserStatusResponse create(UserStatus status, boolean online) {
    return UserStatusResponse.builder()
        .id(status.getUserStatusId())
        .createdAt(status.getCreatedAt())
        .updatedAt(status.getUpdatedAt())
        .userId(status.getUserId())
        .lastActiveAt(status.getLastActiveAt())
        .online(online).build();
  }

}