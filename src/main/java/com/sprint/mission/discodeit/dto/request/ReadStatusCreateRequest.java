package com.sprint.mission.discodeit.dto.request;

import java.util.UUID;

public record ReadStatusCreateRequest(
        UUID userKey,
        UUID channelKey
) {
}
