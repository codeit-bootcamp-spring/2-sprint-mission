package com.sprint.mission.discodeit.dto.userstatus;

import java.time.Instant;
import java.util.UUID;

public record UserStatusTimeUpdateByUserIdRequest(
        UUID userId,
        Instant updateTime

) {
}
