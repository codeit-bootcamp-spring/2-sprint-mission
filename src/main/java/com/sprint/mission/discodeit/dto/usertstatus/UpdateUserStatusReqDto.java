package com.sprint.mission.discodeit.dto.usertstatus;

import java.time.Instant;
import java.util.UUID;

public record UpdateUserStatusReqDto(
        Instant lastOnlineTime
) {

}
