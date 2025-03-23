package com.sprint.mission.discodeit.DTO;

import java.time.LocalDateTime;
import java.util.UUID;

public record UserStatusDTO(
        UUID userId,
        LocalDateTime lastOnlineAt) {
}
