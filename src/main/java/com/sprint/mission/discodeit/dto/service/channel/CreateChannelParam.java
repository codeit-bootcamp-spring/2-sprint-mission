package com.sprint.mission.discodeit.dto.service.channel;

import com.sprint.mission.discodeit.entity.ChannelType;


public record CreateChannelParam(
        ChannelType type,
        String name,
        String description
){
}
