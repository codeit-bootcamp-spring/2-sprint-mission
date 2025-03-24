package com.sprint.mission.discodeit.service.dto.channel;

import com.sprint.mission.discodeit.entity.ChannelType;

public record PublicChannelRequest(
        ChannelType type,
        String name,
        String description
) {
}
