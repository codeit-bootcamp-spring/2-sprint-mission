package com.sprint.mission.discodeit.dto.create;

import java.util.List;
import java.util.UUID;

public record PrivateChannelCreateRequestDTO(
        List<UUID> participantIds
) {
}
