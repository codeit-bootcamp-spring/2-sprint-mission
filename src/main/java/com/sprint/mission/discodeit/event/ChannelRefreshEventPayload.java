package com.sprint.mission.discodeit.event;

import java.util.List;
import java.util.UUID;

public record ChannelRefreshEventPayload(
        List<String> userIds,
        UUID channelId
) {
}