package com.sprint.mission.discodeit.controller.dto;

import java.util.UUID;

public record IdResponse(
        boolean success,
        UUID id
) {
}
