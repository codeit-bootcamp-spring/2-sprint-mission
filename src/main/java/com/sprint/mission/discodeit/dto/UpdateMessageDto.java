package com.sprint.mission.discodeit.dto;

import java.util.UUID;

public record UpdateMessageDto(
        UUID messageKey,
        String content

) {
}
