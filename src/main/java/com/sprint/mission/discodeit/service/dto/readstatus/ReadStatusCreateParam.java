package com.sprint.mission.discodeit.service.dto.readstatus;

import java.util.List;
import java.util.UUID;

public record ReadStatusCreateParam(
        List<UUID> userIds,
        UUID channelId
) {
}
