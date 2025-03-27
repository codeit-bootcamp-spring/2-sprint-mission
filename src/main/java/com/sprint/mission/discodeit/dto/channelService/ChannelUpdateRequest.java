package com.sprint.mission.discodeit.dto.channelService;

import com.sprint.mission.discodeit.entity.ChannelType;


public record ChannelUpdateRequest(
        String newName,
        ChannelType type
) {
}
