package com.sprint.mission.discodeit.application.dto.message;

import java.util.UUID;

public record MessageCreationRequest(String context, UUID chanelId, UUID userId) {
}
