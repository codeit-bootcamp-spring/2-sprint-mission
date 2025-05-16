package com.sprint.mission.discodeit.service.dto.request.channeldto;

import java.util.List;
import java.util.UUID;

public record ChannelCreatePrivateDto(
        List<UUID> participantIds
) {
}
