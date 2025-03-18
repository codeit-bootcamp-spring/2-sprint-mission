package com.sprint.mission.discodeit.DTO.Request;

import com.sprint.mission.discodeit.entity.ChannelType;

import java.util.UUID;

public record ChannelCreateDTO(
        UUID serverId,
        UUID creatorId,
        String name,
        ChannelType type
) {
}
