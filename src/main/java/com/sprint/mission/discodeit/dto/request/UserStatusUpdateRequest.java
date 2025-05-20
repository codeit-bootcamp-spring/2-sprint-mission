package com.sprint.mission.discodeit.dto.request;

import jakarta.validation.constraints.NotNull;

import java.time.Instant;

public record UserStatusUpdateRequest(
    @NotNull(message = "마지막 활동시간에는 null값이 허용되지 않습니다.")
    Instant newLastActiveAt
) {

}
