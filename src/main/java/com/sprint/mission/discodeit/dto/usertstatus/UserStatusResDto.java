package com.sprint.mission.discodeit.dto.usertstatus;

import java.util.UUID;

public record UserStatusResDto(
        UUID id,
        UUID userId,
        boolean isOnline
) {
}
