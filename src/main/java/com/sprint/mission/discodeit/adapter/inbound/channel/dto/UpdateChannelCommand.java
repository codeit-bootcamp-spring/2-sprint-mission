package com.sprint.mission.discodeit.adapter.inbound.channel.dto;

import com.sprint.mission.discodeit.core.channel.entity.ChannelType;
import java.util.UUID;

public record UpdateChannelCommand(
    UUID channelId,
    String replaceName,
    ChannelType replaceType
) {

}
