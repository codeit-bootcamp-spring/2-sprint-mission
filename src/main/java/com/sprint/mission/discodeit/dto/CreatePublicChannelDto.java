package com.sprint.mission.discodeit.dto;

import com.sprint.mission.discodeit.entity.ChannelType;

public record CreatePublicChannelDto(
        ChannelType type,
        String channelName,
        String introduction
) {
}
