package com.sprint.mission.discodeit.service.dto.messagedto;

import java.util.UUID;

public record MessageCreateDto(
        String message,
        UUID channelId,
        UUID senderId
) {

}
