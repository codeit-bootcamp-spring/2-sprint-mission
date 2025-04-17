package com.sprint.mission.discodeit.dto.service.readstatus;

import jakarta.validation.constraints.NotBlank;
import java.time.Instant;
import java.util.UUID;

public record ReadStatusCreateRequest(
    @NotBlank UUID userId,
    @NotBlank UUID channelId,
    @NotBlank Instant lastReadAt
) {

}