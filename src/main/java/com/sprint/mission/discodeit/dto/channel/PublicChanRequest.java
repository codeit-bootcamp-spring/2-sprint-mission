package com.sprint.mission.discodeit.dto.channel;

import com.sprint.mission.discodeit.entity.ChannelType;

public record PublicChanRequest(
        String name,
        String description
) {
}
