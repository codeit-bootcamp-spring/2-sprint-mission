package com.sprint.mission.discodeit.dto.request;

import jakarta.validation.constraints.NotNull;
import java.time.Instant;

public record ReadStatusUpdateRequest(
    @NotNull(message = "newLastReadAt 값은 null 일 수 없습니다.")
    Instant newLastReadAt
) {

}
