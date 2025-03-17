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
    public static ChannelDTO create(UUID serverId,
                                    UUID creatorId,
                                    String name,
                                    ChannelType type) {
        return ChannelDTO.builder()
                .serverId(serverId)
                .creatorId(creatorId)
                .name(name)
                .type(type).build();
    }

    public static ChannelDTO join(UUID userId,
                                  UUID channelId,
                                  ChannelType type) {
        return ChannelDTO.builder()
                .creatorId(userId)
                .channelId(channelId)
                .type(type).build();
    }

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

    public static ChannelDTO update(UUID replaceChannelId,
                                    String replaceName,
                                    ChannelType replaceType) {
        return ChannelDTO.builder()
                .channelId(replaceChannelId)
                .name(replaceName)
                .type(replaceType).build();
    }

    public static ChannelDTO ids(UUID serverId,
                                 UUID userId,
                                 UUID channelId) {
        return ChannelDTO.builder()
                .serverId(serverId)
                .creatorId(userId)
                .channelId(channelId).build();
    }
}
