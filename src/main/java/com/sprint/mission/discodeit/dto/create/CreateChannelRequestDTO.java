package com.sprint.mission.discodeit.dto.create;

import com.sprint.mission.discodeit.entity.ChannelType;

import java.util.UUID;

public record CreateChannelRequestDTO(
        UUID serverId,
        String name,
        ChannelType type
) {
}
