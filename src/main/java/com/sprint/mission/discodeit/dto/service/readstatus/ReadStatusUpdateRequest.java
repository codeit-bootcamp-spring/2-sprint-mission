package com.sprint.mission.discodeit.dto.service.readstatus;

import jakarta.validation.constraints.NotNull;
import java.time.Instant;

public record ReadStatusUpdateRequest(
    @NotNull Instant newLastReadAt
) {

}
