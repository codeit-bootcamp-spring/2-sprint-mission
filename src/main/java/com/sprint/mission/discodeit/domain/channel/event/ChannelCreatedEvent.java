package com.sprint.mission.discodeit.domain.channel.event;

import java.util.UUID;

public record ChannelCreatedEvent(
    UUID channelId
) {

}
