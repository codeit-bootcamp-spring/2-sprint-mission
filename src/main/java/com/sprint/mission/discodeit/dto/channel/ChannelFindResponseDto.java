package com.sprint.mission.discodeit.dto.channel;

import com.sprint.mission.discodeit.entity.channel.Channel;
import com.sprint.mission.discodeit.entity.channel.ChannelType;

import java.time.Instant;
import java.util.List;
import java.util.UUID;

public record ChannelFindResponseDto(
        UUID id,
        Instant createdAt,
        Instant updatedAt,
        ChannelType type,
        String name,
        String description,
        Instant latestMessageAt,
        List<UUID> participantIds

) {
    public static ChannelFindResponseDto fromEntity(Channel channel, Instant latestMessageAt, List<UUID> participantIds) {
        return new ChannelFindResponseDto(
                channel.getId(),
                channel.getCreatedAt(),
                channel.getUpdatedAt(),
                channel.getType(),
                channel.getName(),
                channel.getDescription(),
                latestMessageAt,
                participantIds
        );
    }
}
