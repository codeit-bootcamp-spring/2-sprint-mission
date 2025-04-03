package com.sprint.mission.discodeit.service.dto.readstatusdto;

import java.time.Instant;
import java.util.UUID;

public record ReadStatusUpdateDto(
        Instant newLastReadTime


) {

}
