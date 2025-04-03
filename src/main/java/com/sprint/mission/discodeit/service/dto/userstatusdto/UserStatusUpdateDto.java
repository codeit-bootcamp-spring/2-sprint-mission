package com.sprint.mission.discodeit.service.dto.userstatusdto;

import java.time.Instant;
import java.util.UUID;

public record UserStatusUpdateDto(
        Instant newLastActiveAt

) {

}
