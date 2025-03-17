package com.sprint.mission.discodeit.DTO.Channel;

import com.sprint.mission.discodeit.entity.ChannelType;
import lombok.Builder;

import java.time.Instant;
import java.util.List;
import java.util.UUID;

@Builder
public record ChannelDTO(
        UUID serverId,
        UUID creatorId,
        UUID channelId,
        String name,
        ChannelType type,
        List<UUID> usersIds,
        Instant lastlyMessageAt
) {
    public static ChannelDTO find(UUID channelId,
                                  String name,
                                  List<UUID> usersId,
                                  Instant lastlyMessageAt) {
        return ChannelDTO.builder()
                .channelId(channelId)
                .name(name)
                .usersIds(usersId)
                .lastlyMessageAt(lastlyMessageAt).build();
    }
}
