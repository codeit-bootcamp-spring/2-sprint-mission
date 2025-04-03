package com.sprint.mission.discodeit.dto.service.channel;

import com.sprint.mission.discodeit.entity.ChannelType;

import java.time.Instant;
import java.util.List;
import java.util.UUID;

public record UpdateChannelDTO(
    UUID id,
    ChannelType type,
    String name,
    String description,
    List<UUID> participantIds,
    Instant lastMessageAt
) {

}
