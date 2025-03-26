package com.sprint.mission.discodeit.dto.create;

import java.util.UUID;

public record PublicChannelCreateRequestDTO(
        UUID serverId,
        String name
) {
}
