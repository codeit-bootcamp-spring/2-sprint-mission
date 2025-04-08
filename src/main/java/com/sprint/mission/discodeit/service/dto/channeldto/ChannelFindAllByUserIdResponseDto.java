package com.sprint.mission.discodeit.service.dto.channeldto;

import com.sprint.mission.discodeit.entity.Channel;
import com.sprint.mission.discodeit.entity.ChannelType;

import java.time.Instant;
import java.util.List;
import java.util.UUID;

public record ChannelFindAllByUserIdResponseDto(
        UUID id,
        ChannelType type,
        String name,
        String description,
        Instant createdAt,
        List<UUID> participantIds,
        Instant lastMessageAt


) {
    public static ChannelFindAllByUserIdResponseDto fromChannel(Channel channel, List<UUID> participantIds, Instant lastMessageAt) {
        return new ChannelFindAllByUserIdResponseDto(
                channel.getId(),
                channel.getType(),
                channel.getName(),
                channel.getDescription(),
                channel.getCreatedAt(),
                participantIds,
                lastMessageAt
        );
    }

}