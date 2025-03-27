package com.sprint.mission.discodeit.service.dto.channeldto;

import com.sprint.mission.discodeit.entity.Channel;
import com.sprint.mission.discodeit.entity.ChannelType;
import com.sprint.mission.discodeit.entity.ReadStatus;

import java.time.Instant;
import java.util.UUID;

public record ChannelFindResponseDto(
        UUID id,
        ChannelType channelType,
        String name,
        String description,
        Instant createdAt,
        Instant updatedAt,
        UUID userId,
        Instant lastReadTime
) {
    public static ChannelFindResponseDto fromChannel(Channel channel, ReadStatus readStatus) {
        UUID userId = (channel.getType() == ChannelType.PRIVATE && readStatus !=null) ? readStatus.getUserId() : null;
        return new ChannelFindResponseDto(
                channel.getId(),
                channel.getType(),
                channel.getChannelName(),
                channel.getDescription(),
                channel.getCreatedAt(),
                channel.getUpdatedAt(),
                userId,
                readStatus != null ? readStatus.getLastReadTime() : null

        );
    }
}