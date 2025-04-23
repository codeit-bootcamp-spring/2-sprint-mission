package com.sprint.mission.discodeit.dto.channel;

import com.sprint.mission.discodeit.dto.user.UserDto;
import com.sprint.mission.discodeit.entity.Channel;
import com.sprint.mission.discodeit.entity.ChannelType;
import java.time.Instant;
import java.util.List;
import java.util.UUID;

public record ChannelDto(
        UUID id,
        String name,
        String description,
        ChannelType type,
        Instant lastMessageAt,
        List<UserDto> participants
) {
    public ChannelDto(Channel channel, Instant lastMessageAt, List<UserDto> participants) {
        this(channel.getId(), channel.getName(), channel.getDescription(), channel.getType(), lastMessageAt,
                participants);
    }
}
