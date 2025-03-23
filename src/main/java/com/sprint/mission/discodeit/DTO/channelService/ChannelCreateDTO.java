package com.sprint.mission.discodeit.DTO.channelService;

import com.sprint.mission.discodeit.entity.Channel;
import com.sprint.mission.discodeit.entity.ChannelType;


public record ChannelCreateDTO(
        ChannelType type,
        String name
) {
    public static ChannelCreateDTO toDTO(Channel channel) {
        return new ChannelCreateDTO(channel.getType(), channel.getChannelName());
    }

    public Channel toEntity() {
        return new Channel(this.type, this.name);
    }
}
