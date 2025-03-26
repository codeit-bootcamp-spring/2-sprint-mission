package com.sprint.mission.discodeit.service.dto.channeldto;

import com.sprint.mission.discodeit.entity.ChannelType;

import java.util.UUID;

public record ChannelCreateDto(

        ChannelType channelType,
        UUID userId,
        String channelName,
        String description

) {
}
