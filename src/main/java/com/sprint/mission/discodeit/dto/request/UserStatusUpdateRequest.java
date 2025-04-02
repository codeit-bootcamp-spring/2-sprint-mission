package com.sprint.mission.discodeit.dto.request;

import io.swagger.v3.oas.annotations.media.Schema;

import java.time.Instant;

public record UserStatusUpdateRequest(
        @Schema(description = "마지막 활동 시간")
        Instant newLastActiveAt
) {
}
