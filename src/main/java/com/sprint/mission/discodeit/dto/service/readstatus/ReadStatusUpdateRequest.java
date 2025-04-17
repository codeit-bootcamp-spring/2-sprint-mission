package com.sprint.mission.discodeit.dto.service.readstatus;

import jakarta.validation.constraints.NotBlank;
import java.time.Instant;

public record ReadStatusUpdateRequest(
    @NotBlank Instant newLastReadAt
) {

}
