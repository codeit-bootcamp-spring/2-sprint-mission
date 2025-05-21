package com.sprint.mission.discodeit.dto.request;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

import java.time.Instant;
import java.util.UUID;

public record ReadStatusCreateRequest(
    @NotNull
    UUID userId,
    @NotNull
    UUID channelId,
    @NotBlank
    Instant lastReadAt
) {

}
