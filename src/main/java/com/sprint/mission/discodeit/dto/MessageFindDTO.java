package com.sprint.mission.discodeit.dto;

import java.time.Instant;

public record MessageFindDTO(
    String userName,
    String text,
    Instant createdAt
) {
}
