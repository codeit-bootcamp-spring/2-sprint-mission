package com.sprint.mission.discodeit.service.dto.readstatus;

import jakarta.validation.constraints.NotBlank;
import java.time.Instant;
import java.util.UUID;

public record ReadStatusCreateRequest(
    @NotBlank UUID userId,
    @NotBlank UUID channelId,
    @NotBlank Instant lastReadAt
) {

}