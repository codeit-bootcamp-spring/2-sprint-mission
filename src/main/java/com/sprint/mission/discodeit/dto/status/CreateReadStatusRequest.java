package com.sprint.mission.discodeit.dto.status;

import java.util.UUID;

public record CreateReadStatusRequest(
        UUID userId,
        UUID channelId
) {}
