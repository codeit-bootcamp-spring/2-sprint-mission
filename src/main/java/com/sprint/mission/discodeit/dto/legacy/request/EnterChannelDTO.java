package com.sprint.mission.discodeit.dto.legacy.request;

import com.sprint.mission.discodeit.entity.ChannelType;

public record EnterChannelDTO(
        String userId,
        ChannelType type
) {
}
