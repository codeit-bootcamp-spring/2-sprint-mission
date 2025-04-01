package com.sprint.mission.discodeit.service.dto.readstatusdto;

import java.nio.file.Path;
import java.time.Instant;
import java.util.UUID;

public record ReadStatusCreateDto(
       UUID userId,
       UUID channelId,
       Instant lastReadTime
) {

}
