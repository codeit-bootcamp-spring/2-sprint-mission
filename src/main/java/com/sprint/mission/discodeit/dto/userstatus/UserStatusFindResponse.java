package com.sprint.mission.discodeit.dto.userstatus;

import java.util.UUID;

public record UserStatusFindResponse(
        UUID id,
        UUID userId
) {
}
