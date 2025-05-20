package com.sprint.mission.discodeit.dto.request;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotNull;

import java.time.Instant;

public record UserStatusUpdateRequest(
        @Schema(description = "마지막 활동 시간")
        @NotNull(message = "새로운 마지막 활동 시각은 필수입니다.")
        Instant newLastActiveAt
) {
}
