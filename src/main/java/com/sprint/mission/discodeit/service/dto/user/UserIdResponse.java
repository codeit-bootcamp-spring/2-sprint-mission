package com.sprint.mission.discodeit.service.dto.user;

import java.util.UUID;

public record UserIdResponse(
        boolean success,
        UUID id
) {
}
