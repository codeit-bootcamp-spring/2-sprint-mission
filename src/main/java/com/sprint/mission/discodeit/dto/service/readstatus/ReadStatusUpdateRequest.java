package com.sprint.mission.discodeit.dto.service.readstatus;

import jakarta.validation.constraints.NotEmpty;
import java.time.Instant;

public record ReadStatusUpdateRequest(
    @NotEmpty Instant newLastReadAt
) {

}
