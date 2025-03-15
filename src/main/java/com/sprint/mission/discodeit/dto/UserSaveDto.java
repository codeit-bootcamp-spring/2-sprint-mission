package com.sprint.mission.discodeit.dto;

import java.time.Instant;
import java.util.UUID;

public record UserSaveDto(
        UUID userUUID,
        String nickname,
        UUID profile,
        Instant createAt
) {
}
