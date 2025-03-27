package com.sprint.mission.discodeit.service.dto.channeldto;

import com.sprint.mission.discodeit.entity.ChannelType;

import java.rmi.server.UID;
import java.util.List;
import java.util.UUID;

public record ChannelCreateDto(

        ChannelType channelType,
        List<UUID> userId,
        String channelName,
        String description

) {
}
