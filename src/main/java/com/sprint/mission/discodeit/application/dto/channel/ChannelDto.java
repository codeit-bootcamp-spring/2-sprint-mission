package com.sprint.mission.discodeit.application.dto.channel;

import com.sprint.mission.discodeit.entity.Channel;
import com.sprint.mission.discodeit.entity.ChannelType;

import java.time.Instant;
import java.util.UUID;

public record ChannelDto(UUID id, String name, ChannelType type, Instant lastMessageCreatedAt) {
    public static ChannelDto fromEntity(Channel channel, Instant lastMessageCreatedAt) {
        return new ChannelDto(channel.getId(), channel.getName(), channel.getType(), lastMessageCreatedAt);
    }
}
