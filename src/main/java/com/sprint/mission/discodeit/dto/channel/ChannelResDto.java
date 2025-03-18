package com.sprint.mission.discodeit.dto.channel;

import com.sprint.mission.discodeit.entity.Channel;
import com.sprint.mission.discodeit.entity.ChannelType;

import java.time.Instant;
import java.util.List;
import java.util.UUID;

public record ChannelResDto(
        UUID id,
        String title,
        String description,
        ChannelType channelType,
        Instant latestMessageAt,
        List<UUID>participantUserIds
) {
}
