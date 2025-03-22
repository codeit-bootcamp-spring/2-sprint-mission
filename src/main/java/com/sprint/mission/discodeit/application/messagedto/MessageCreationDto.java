package com.sprint.mission.discodeit.application.messagedto;

import java.util.UUID;

public record MessageCreationDto(String context, UUID chanelId, UUID userId) {
}
