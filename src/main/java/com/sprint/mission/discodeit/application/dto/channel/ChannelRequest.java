package com.sprint.mission.discodeit.application.dto.channel;

import com.sprint.mission.discodeit.entity.Channel;
import com.sprint.mission.discodeit.entity.ChannelType;

import java.time.Instant;
import java.util.List;
import java.util.UUID;

public record ChannelRequest(UUID id, String name, ChannelType type, Instant lastMessageCreatedAt,
                             List<UUID> privateMemberIds) {

    public static ChannelRequest fromPrivate(Channel channel, Instant lastMessageCreatedAt, List<UUID> privateMemberIds) {
        return new ChannelRequest(channel.getId(), channel.getName(), channel.getType(), lastMessageCreatedAt, privateMemberIds);
    }

    public static ChannelRequest fromPublic(Channel channel, Instant lastMessageCreatedAt) {
        return new ChannelRequest(channel.getId(), channel.getName(), channel.getType(), lastMessageCreatedAt, null);
    }
}