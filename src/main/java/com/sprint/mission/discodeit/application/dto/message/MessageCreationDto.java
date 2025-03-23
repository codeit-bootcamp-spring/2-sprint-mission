package com.sprint.mission.discodeit.application.dto.message;

import java.util.UUID;

public record MessageCreationDto(String context, UUID chanelId, UUID userId) {
}
