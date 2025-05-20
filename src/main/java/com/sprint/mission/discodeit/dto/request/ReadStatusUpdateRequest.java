package com.sprint.mission.discodeit.dto.request;

import jakarta.validation.constraints.NotNull;

import java.time.Instant;

public record ReadStatusUpdateRequest(
        @NotNull(message = "새로운 읽음 시각은 필수입니다.")
        Instant newLastReadAt
) {
}
