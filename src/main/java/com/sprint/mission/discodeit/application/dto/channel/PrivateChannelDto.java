package com.sprint.mission.discodeit.application.dto.channel;

import com.sprint.mission.discodeit.entity.Channel;
import com.sprint.mission.discodeit.entity.ChannelType;

import java.time.Instant;
import java.util.List;
import java.util.UUID;

public record PrivateChannelDto(UUID id, String name, ChannelType type, Instant lastMessageCreatedAt,
                                List<UUID> privateMemberIds) implements ChannelDto {
    @Override
    public UUID id() {
        return id;
    }

    @Override
    public String name() {
        return name;
    }

    @Override
    public ChannelType type() {
        return type;
    }

    @Override
    public Instant lastMessageCreatedAt() {
        return lastMessageCreatedAt;
    }

    public static ChannelDto fromPrivateChannel(Channel channel, Instant lastMessageCreatedAt, List<UUID> privateMemberIds) {
        return new PrivateChannelDto(channel.getId(), channel.getName(), channel.getType(), lastMessageCreatedAt, privateMemberIds);
    }
}
