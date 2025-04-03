package com.sprint.mission.discodeit.core.channel.usecase.dto;

import java.util.UUID;

public record UpdateChannelCommand(
    UUID channelId,
    String newName,
    String description
//    ChannelType newType
) {

}
