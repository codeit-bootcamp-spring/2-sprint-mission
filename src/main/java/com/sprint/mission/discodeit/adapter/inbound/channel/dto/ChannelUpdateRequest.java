package com.sprint.mission.discodeit.adapter.inbound.channel.dto;

import com.sprint.mission.discodeit.core.channel.entity.ChannelType;

public record ChannelUpdateRequest(
    String newName,
    ChannelType newType
) {

}
