package com.sprint.mission.application;

import java.util.UUID;

public record MessageDto(UUID messageId, String context) {
}
