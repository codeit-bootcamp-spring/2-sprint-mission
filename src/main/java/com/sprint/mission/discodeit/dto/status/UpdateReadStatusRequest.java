package com.sprint.mission.discodeit.dto.status;

import jakarta.validation.constraints.NotNull;
import java.time.Instant;

public record UpdateReadStatusRequest(
    @NotNull(message = "읽은 시각은 필수입니다.")
    Instant newLastReadAt
) {

}
