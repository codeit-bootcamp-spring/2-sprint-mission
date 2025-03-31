package com.sprint.mission.discodeit.dto.channelService;

import java.util.List;
import java.util.UUID;

public record ChannelCreateByPrivateRequest(
        List<UUID> UserIds
) {
}
