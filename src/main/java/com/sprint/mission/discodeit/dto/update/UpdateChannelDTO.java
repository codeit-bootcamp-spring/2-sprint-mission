package com.sprint.mission.discodeit.dto.update;

import com.sprint.mission.discodeit.entity.ChannelType;

public record UpdateChannelDTO(
        String replaceName,
        ChannelType replaceType
) {
}
