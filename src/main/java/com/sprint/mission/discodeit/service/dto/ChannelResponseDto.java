package com.sprint.mission.discodeit.service.dto;

import com.sprint.mission.discodeit.entity.Channel;
import com.sprint.mission.discodeit.entity.ChannelType;
import com.sprint.mission.discodeit.entity.UserRole;

import java.time.Instant;
import java.util.Set;
import java.util.UUID;

public record ChannelResponseDto(
        UUID id,
        ChannelType type,
        String category,
        String name,
        Set<UUID> userMembers,
        UserRole writePermission,
        UUID userId,
        Instant lastMessageCreatedAt
) {
    public static ChannelResponseDto convertToResponseDto(Channel channel, Set<UUID> channelMembers, Instant lastMessageCreatedAt) {
        return new ChannelResponseDto(channel.getId(), channel.getType(), channel.getCategory(), channel.getName(), channelMembers, channel.getWritePermission(), channel.getUserId(), lastMessageCreatedAt);
    }
}
