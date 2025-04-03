package com.sprint.mission.discodeit.dto.request;

import java.util.List;
import java.util.UUID;

public record MessageCreateRequest(
        String content,
        UUID channelKey,
        UUID authorKey,
        List<BinaryContentCreateRequest> binaryContentRequests
) {
}
