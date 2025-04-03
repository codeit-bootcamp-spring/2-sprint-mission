package com.sprint.mission.discodeit.adapter.inbound.status.request;

import jakarta.validation.constraints.NotBlank;
import java.time.Instant;
import java.util.UUID;

public record ReadStatusCreateRequest(
    @NotBlank UUID userId,
    @NotBlank UUID channelId,
    Instant lastReadAt
) {

}
