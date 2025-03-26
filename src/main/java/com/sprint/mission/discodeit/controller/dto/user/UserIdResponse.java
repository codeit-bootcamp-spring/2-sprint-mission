package com.sprint.mission.discodeit.controller.dto.user;

import java.util.UUID;

public record UserIdResponse(
        boolean success,
        UUID id
) {
}
