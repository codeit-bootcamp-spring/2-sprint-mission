package com.sprint.mission.discodeit.dto.service.channel;

import java.util.UUID;

public record UpdateChannelParam(
        UUID id,
        String name,
        String description
) {
}
