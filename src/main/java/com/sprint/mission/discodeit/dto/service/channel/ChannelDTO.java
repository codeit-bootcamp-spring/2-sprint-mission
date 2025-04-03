package com.sprint.mission.discodeit.dto.service.channel;

import com.sprint.mission.discodeit.entity.ChannelType;

import java.time.Instant;
import java.util.UUID;


public record ChannelDTO(
        UUID id,
        Instant createdAt,
        Instant updatedAt,
        ChannelType type,
        String name,
        String description
) {
}
