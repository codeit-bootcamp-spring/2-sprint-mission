package com.sprint.mission.discodeit.service.dto.readstatus;

import jakarta.validation.constraints.NotBlank;
import java.time.Instant;

public record ReadStatusUpdateRequest(
    @NotBlank Instant newLastReadAt
) {

}
