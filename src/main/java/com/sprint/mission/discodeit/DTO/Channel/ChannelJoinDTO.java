package com.sprint.mission.discodeit.DTO.Channel;

import com.sprint.mission.discodeit.entity.ChannelType;

public record ChannelJoinDTO (
        String userId,
        String channelId,
        ChannelType type
){
}
