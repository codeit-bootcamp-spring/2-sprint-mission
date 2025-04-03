package com.sprint.mission.discodeit.adapter.inbound.channel.request;

import com.sprint.mission.discodeit.core.channel.entity.ChannelType;

public record ChannelUpdateRequest(
    String newName,
    ChannelType newType
) {

}
