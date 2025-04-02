package com.sprint.mission.discodeit.dto.channel.request;

import jakarta.validation.constraints.NotNull;

import java.time.Instant;
import java.util.UUID;

public record ChannelUpdateDto(
        @NotNull
        UUID id,

        @NotNull
        String name,
        @NotNull
        String description
) {
}
