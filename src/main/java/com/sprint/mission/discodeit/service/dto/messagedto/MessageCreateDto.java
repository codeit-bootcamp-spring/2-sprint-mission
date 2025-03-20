package com.sprint.mission.discodeit.service.dto.messagedto;

import java.nio.file.Path;
import java.util.List;
import java.util.UUID;

public record MessageCreateDto(
        String message,
        UUID channelId,
        UUID senderId,
        List<Path> attachmentPath
) {

}
