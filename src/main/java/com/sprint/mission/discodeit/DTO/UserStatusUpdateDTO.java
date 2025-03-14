package com.sprint.mission.discodeit.DTO;

import java.util.UUID;

public record UserStatusUpdateDTO(
        UUID userId,
        UUID replaceId
) {
}
