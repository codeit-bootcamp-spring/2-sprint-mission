package com.sprint.mission.discodeit.dto.channelService;

import com.sprint.mission.discodeit.entity.Channel;
import com.sprint.mission.discodeit.entity.ChannelType;


public record ChannelCreateRequest(
        ChannelType type,
        String name
) {
    public static ChannelCreateRequest toDTO(Channel channel) {
        return new ChannelCreateRequest(channel.getType(), channel.getChannelName());
    }

    public Channel toEntity() {
        return new Channel(this.type, this.name);
    }
}
