package com.sprint.mission.discodeit.dto.readstatus;

import java.time.Instant;

public record UpdateReadStatusReqDto(
        Instant lastReadAt
) {
}
