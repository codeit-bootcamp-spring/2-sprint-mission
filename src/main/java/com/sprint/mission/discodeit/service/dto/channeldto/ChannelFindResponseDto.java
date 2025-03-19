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
        if (channel.getType() == ChannelType.PRIVATE) {
            return new ChannelFindResponseDto(
                    channel.getId(),
                    channel.getType(),
                    channel.getChannelName(),
                    channel.getDescription(),
                    channel.getCreatedAt(),
                    channel.getUpdatedAt(),
                    readStatus != null ? readStatus.getUserId() : null,
                    readStatus != null ? readStatus.getLastReadTime() : null

            );
        } else {
            return new ChannelFindResponseDto(
                    channel.getId(),
                    channel.getType(),
                    channel.getChannelName(),
                    channel.getDescription(),
                    channel.getCreatedAt(),
                    channel.getUpdatedAt(),
                    null,  // PUBLIC 일 때 userId는 null
                    readStatus != null ? readStatus.getLastReadTime() : null
            );
        }
    }
}