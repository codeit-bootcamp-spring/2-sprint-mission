package com.sprint.mission.discodeit.application.dto.channel;

import java.util.UUID;

public record PublicChannelUpdateRequest(UUID channelId, String channelName) {
}
