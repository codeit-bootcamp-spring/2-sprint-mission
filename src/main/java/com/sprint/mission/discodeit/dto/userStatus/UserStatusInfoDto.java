package com.sprint.mission.discodeit.dto.userStatus;

import java.time.Instant;
import java.util.UUID;

public record UserStatusInfoDto(
    UUID id,
    UUID userid,
    boolean status,
    Instant updatedAt
) {

}
