package com.sprint.mission.discodeit.dto.request;

import jakarta.validation.constraints.NotNull;
import java.time.Instant;

public record UserStatusUpdateRequest(
    @NotNull(message = "최근 활동 시간은 필수 입력 값입니다.")
    Instant newLastActiveAt
) {

}
