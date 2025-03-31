package com.sprint.mission.discodeit.dto.channelService;

import com.sprint.mission.discodeit.entity.Channel;
import com.sprint.mission.discodeit.entity.ChannelType;

import java.time.Instant;
import java.util.List;
import java.util.UUID;

public record ChannelFindDto(
        UUID id,
        ChannelType channelType,
        List<UUID> userIds,
        Instant lastedAt
) {

    public ChannelFindDto toDto(Channel channel) {
        return new ChannelFindDto(id, channelType, userIds, lastedAt);
    }
}
