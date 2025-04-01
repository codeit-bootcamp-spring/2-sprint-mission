package com.sprint.mission.discodeit.adapter.inbound.channel.dto;

import com.sprint.mission.discodeit.core.channel.entity.ChannelType;

public record UpdateChannelDTO(
    String replaceName,
    ChannelType replaceType
) {

}
