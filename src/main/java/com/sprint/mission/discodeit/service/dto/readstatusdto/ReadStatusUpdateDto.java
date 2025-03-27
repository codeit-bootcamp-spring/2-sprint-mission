package com.sprint.mission.discodeit.service.dto.readstatusdto;

import java.nio.file.Path;
import java.time.Instant;
import java.util.UUID;

public record ReadStatusUpdateDto(
        UUID userId,
        UUID channelId,
        Instant newLastReadTime


) {

}
