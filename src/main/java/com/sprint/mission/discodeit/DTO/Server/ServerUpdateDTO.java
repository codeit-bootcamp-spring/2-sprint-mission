package com.sprint.mission.discodeit.DTO.Server;

import java.util.UUID;

public record ServerUpdateDTO(
        UUID replaceServerId,
        UUID replaceOwnerId,
        String replaceName
) {
}
