package com.sprint.mission.discodeit.application.dto.channel;

import com.sprint.mission.discodeit.entity.Channel;
import com.sprint.mission.discodeit.entity.ChannelType;

import java.time.Instant;
import java.util.List;
import java.util.UUID;

public record ChannelResult(UUID id, ChannelType type, String name, String description,
                            Instant lastMessageAt,
                            List<UUID> participantIds) {

    public static ChannelResult fromPrivate(Channel channel, Instant lastMessageCreatedAt, List<UUID> privateMemberIds) {
        return new ChannelResult(channel.getId(),
                channel.getType(),
                null,
                null,
                lastMessageCreatedAt,
                privateMemberIds);
    }

    public static ChannelResult fromPublic(Channel channel, Instant lastMessageCreatedAt) {
        return new ChannelResult(channel.getId(), channel.getType(), channel.getName(), channel.getDescription(), lastMessageCreatedAt, null);
    }
}