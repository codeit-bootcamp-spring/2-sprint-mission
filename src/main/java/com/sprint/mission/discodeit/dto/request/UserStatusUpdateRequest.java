package com.sprint.mission.discodeit.dto.request;

import jakarta.validation.constraints.NotNull;
import java.time.Instant;

public record UserStatusUpdateRequest(

    @NotNull(message = "새로운 시간 입력 필수")
    Instant newLastActiveAt
) {

}
