package com.sprint.mission.discodeit.service.dto.readstatusdto;

import java.util.UUID;

public record ReadStatusFindDto(
        UUID Id,
        UUID userId
) {
}
