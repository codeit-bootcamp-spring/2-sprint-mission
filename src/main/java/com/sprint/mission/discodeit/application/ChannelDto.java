package com.sprint.mission.discodeit.application;

import com.sprint.mission.discodeit.entity.Channel;
import java.util.UUID;

public record ChannelDto(UUID id, String name, UsersDto usersDto) {
    public static ChannelDto fromEntity(Channel channel, UsersDto usersDto) {

        return new ChannelDto(channel.getId(), channel.getName(), usersDto);
    }
}
