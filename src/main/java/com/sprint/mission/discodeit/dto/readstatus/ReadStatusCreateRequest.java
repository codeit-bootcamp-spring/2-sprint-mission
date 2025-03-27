package com.sprint.mission.discodeit.dto.readstatus;

import jakarta.validation.constraints.NotBlank;

import java.util.UUID;

public record ReadStatusCreateRequest(
        @NotBlank
        UUID userId,
        @NotBlank
        UUID channelId
) {
}
