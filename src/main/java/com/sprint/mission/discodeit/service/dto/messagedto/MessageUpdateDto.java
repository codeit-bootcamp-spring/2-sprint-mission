package com.sprint.mission.discodeit.service.dto.messagedto;

import java.nio.file.Path;
import java.util.List;
import java.util.UUID;

public record MessageUpdateDto(
        UUID messageId,
        String changeMessage,
        List<Path> attachmentPath
) {

}
