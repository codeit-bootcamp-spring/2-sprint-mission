package com.sprint.mission.discodeit.service.dto.channeldto;

import com.sprint.mission.discodeit.entity.ChannelType;

import java.util.List;
import java.util.UUID;

public record ChannelCreatePrivateDto(

        ChannelType channelType,
        List<UUID> userId

) {
}
