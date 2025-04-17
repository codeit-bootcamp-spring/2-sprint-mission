package com.sprint.mission.discodeit.dto.controller.readstatus;

import java.time.Instant;

public record UpdateReadStatusRequestDTO(
    Instant newLastReadAt
) {

}
