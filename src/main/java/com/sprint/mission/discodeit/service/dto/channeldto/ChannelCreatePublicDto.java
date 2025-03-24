package com.sprint.mission.discodeit.service.dto.channeldto;

import com.sprint.mission.discodeit.entity.ChannelType;

public record ChannelCreatePublicDto(
        String channelName,
        String description
) {

}
