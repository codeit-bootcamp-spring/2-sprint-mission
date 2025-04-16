package com.sprint.mission.discodeit.dto.data;

import io.swagger.v3.oas.annotations.media.Schema;

import java.time.Instant;
import java.util.UUID;

@Schema(description = "사용자 상태 응답 DTO")
public record UserStatusDto(
        @Schema(description = "사용자 상태 ID")
        UUID id,

        @Schema(description = "사용자 ID")
        UUID userId,

        @Schema(description = "마지막 활동 시간")
        Instant lastActiveAt
) {
}
