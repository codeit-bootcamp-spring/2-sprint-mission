package com.sprint.mission.discodeit.dto.service.channel;

import com.sprint.mission.discodeit.entity.ChannelType;
import lombok.Builder;

import java.time.Instant;
import java.util.List;
import java.util.UUID;

@Builder
public record FindChannelDTO(
        UUID id,
        ChannelType type,
        String name,
        String description,
        Instant lastMessageAt,
        List<UUID> userIds
) {
}
