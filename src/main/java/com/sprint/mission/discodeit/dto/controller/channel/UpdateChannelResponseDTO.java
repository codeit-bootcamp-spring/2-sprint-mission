package com.sprint.mission.discodeit.dto.controller.channel;

import com.sprint.mission.discodeit.entity.ChannelType;

import java.time.Instant;
import java.util.UUID;

public record UpdateChannelResponseDTO(
        UUID id,
        Instant updatedAt,
        ChannelType type,
        String name,
        String description
) {
}
