package com.sprint.mission.discodeit.core.channel.usecase.dto;

import com.sprint.mission.discodeit.core.channel.entity.ChannelType;
import java.util.UUID;

public record UpdateChannelCommand(
    UUID channelId,
    String newName,
    ChannelType newType
) {

}
