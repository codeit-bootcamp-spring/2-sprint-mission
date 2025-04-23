package com.sprint.mission.discodeit.service.dto.request.userstatusdto;

import java.time.Instant;

public record UserStatusUpdateDto(
        Instant newLastActiveAt

) {

}
