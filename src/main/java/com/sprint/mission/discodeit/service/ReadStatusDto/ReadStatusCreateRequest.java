package com.sprint.mission.discodeit.service.ReadStatusDto;

import java.util.UUID;

public record ReadStatusCreateRequest(
        UUID userId,
        UUID channelId,
        UUID lastReadMessageId) {
}
