package com.sprint.mission.discodeit.dto.channelService;

import com.sprint.mission.discodeit.entity.ChannelType;

import java.time.Instant;
import java.util.List;
import java.util.UUID;

public record ChannelDto(
        UUID id,
        ChannelType type,
        String name,
        List<UUID> participantIds,
        Instant lastMessageAt
) {

}
