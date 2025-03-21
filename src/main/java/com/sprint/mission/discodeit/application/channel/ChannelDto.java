package com.sprint.mission.discodeit.application.channel;

import com.sprint.mission.discodeit.application.user.UsersDto;
import com.sprint.mission.discodeit.entity.Channel;
import com.sprint.mission.discodeit.entity.ChannelType;

import java.util.UUID;

public record ChannelDto(UUID id, String name, ChannelType type, UsersDto usersDto) {
    public static ChannelDto fromEntity(Channel channel, UsersDto usersDto) {
        return new ChannelDto(channel.getId(), channel.getName(), channel.getType(), usersDto);
    }
}
