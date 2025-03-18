package com.sprint.mission.discodeit.DTO.Request;

import com.sprint.mission.discodeit.entity.ChannelType;

public record EnterChannelDTO(
        String userId,
        ChannelType type
) {
}
