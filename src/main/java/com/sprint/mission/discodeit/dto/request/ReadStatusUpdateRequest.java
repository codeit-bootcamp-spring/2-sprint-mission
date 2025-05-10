package com.sprint.mission.discodeit.dto.request;

import jakarta.validation.constraints.NotNull;
import java.time.Instant;

public record ReadStatusUpdateRequest(
    @NotNull(message = "마지막으로 읽은 시점 입력 필수")
    Instant newLastReadAt
) {

}
