package com.sprint.mission.discodeit.service.dto.channel;

import com.sprint.mission.discodeit.entity.Channel;
import com.sprint.mission.discodeit.entity.ChannelType;
import java.time.Instant;
import java.util.List;
import java.util.UUID;

public record ChannelByIdResponse(
        UUID channelId,
        Instant createdAt,
        Instant updatedAt,
        ChannelType type,
        String name,
        String description,
        List<UUID> userIdList,
        Instant lastMessageTime
) {
    public static ChannelByIdResponse of(Channel channel, String name, String description, List<UUID> userIds,
                                         Instant lastMessageTime) {
        return new ChannelByIdResponse(
                channel.getId(), channel.getCreatedAt(), channel.getUpdatedAt(),
                channel.getType(), name, description,
                userIds, lastMessageTime
        );
    }
}
