package com.sprint.mission.discodeit.dto.controller.readstatus;

import java.time.Instant;
import java.util.UUID;

public record FindReadStatusResponseDTO(
    UUID id,
    UUID userId,
    UUID channelId,
    Instant lastReadAt
) {

}
