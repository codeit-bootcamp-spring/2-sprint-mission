package com.sprint.mission.discodeit.service.dto.channel;

import com.sprint.mission.discodeit.entity.ChannelType;
import java.util.List;
import java.util.UUID;

public record PrivateChannelRequest(
        ChannelType type,
        List<UUID> userIds
) {
}
