package com.sprint.mission.discodeit.DTO.Channel;

import com.sprint.mission.discodeit.entity.ChannelType;

import java.util.UUID;

public record ChannelUpdateDTO (
        UUID replaceChannelId,
        String replaceName,
        ChannelType replaceType
){
}
