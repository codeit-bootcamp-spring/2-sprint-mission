package com.sprint.mission.discodeit.core.channel.usecase.dto;

import java.util.UUID;

public record ChannelUpdateCommand(
    UUID channelId,
    String newName,
    String newDescription
) {

}
