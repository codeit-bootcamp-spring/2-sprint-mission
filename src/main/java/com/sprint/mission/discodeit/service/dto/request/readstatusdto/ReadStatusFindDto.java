package com.sprint.mission.discodeit.service.dto.request.readstatusdto;

import java.util.UUID;

public record ReadStatusFindDto(
        UUID Id,
        UUID userId
) {
}
