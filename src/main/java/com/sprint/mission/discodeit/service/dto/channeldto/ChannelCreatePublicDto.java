package com.sprint.mission.discodeit.service.dto.channeldto;

import com.sprint.mission.discodeit.entity.ChannelType;

import java.util.List;
import java.util.UUID;

public record ChannelCreatePublicDto(

        ChannelType channelType,
        String channelName,
        String description

) {
}
