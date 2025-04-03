package com.sprint.mission.discodeit.dto.request;

import java.util.UUID;

public record PublicChannelUpdateRequest(
        UUID channelKey,
        String newName,
        String newIntroduction
) {
}
