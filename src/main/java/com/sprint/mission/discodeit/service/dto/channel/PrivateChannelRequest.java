package com.sprint.mission.discodeit.service.dto.channel;

import java.util.List;
import java.util.UUID;

public record PrivateChannelRequest(
        List<UUID> userIds
) {
}
