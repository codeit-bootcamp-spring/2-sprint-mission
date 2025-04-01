package com.sprint.mission.discodeit.dto.request.channel;

import jakarta.validation.constraints.NotBlank;

import java.util.List;
import java.util.UUID;

public record PrivateChannelCreateRequest(
        String name,
        String description,
        @NotBlank
        List<UUID> participantIds
) {
}
