package com.sprint.mission.discodeit.service.dto.channel;

import java.util.Optional;
import java.util.UUID;

public record ChannelUpdateRequest(
        UUID id,
        Optional<String> newName,
        Optional<String> newDescription
) {
}
