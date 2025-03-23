package com.sprint.mission.discodeit.dto.channel;

import java.util.UUID;

public record UpdateChannelRequest(
        UUID channelId,
        String newName,
        String newDescription
) {
}
