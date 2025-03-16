package com.sprint.mission.discodeit.DTO.Channel;

import com.sprint.mission.discodeit.entity.ChannelType;

public record ChannelCreateDTO(
        String serverId,
        String creatorId,
        String name,
        ChannelType type
) {
}
