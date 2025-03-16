package com.sprint.mission.discodeit.DTO.Message;

import java.util.UUID;

public record MessageUpdateDTO(
        UUID replaceId,
        String replaceText
) {
}
