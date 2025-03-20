package com.sprint.mission.discodeit.service.dto.channel;

import java.util.UUID;

public record ChannelUpdateRequest(
        UUID id,
        String newName,
        String newDescription
) {
}
