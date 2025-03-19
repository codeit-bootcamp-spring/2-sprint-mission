package com.sprint.mission.discodeit.service.dto.userdto;

import java.util.UUID;

public record UserDeleteDto(
        UUID userId,
        UUID userStatusId,
        UUID readStatusId,
        UUID binaryContentId
) {
}
