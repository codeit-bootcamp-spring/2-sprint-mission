package com.sprint.mission.discodeit.DTO.Channel;

import com.sprint.mission.discodeit.entity.ChannelType;
import lombok.Builder;

import java.util.UUID;

@Builder
public record ChannelCRUDDTO(
        UUID serverId,
        UUID creatorId,
        UUID channelId,
        String name,
        ChannelType type
) {
    public static ChannelCRUDDTO create(
            UUID serverId,
            UUID creatorId,
            String name,
            ChannelType type
    ) {
        return ChannelCRUDDTO.builder()
                .serverId(serverId)
                .creatorId(creatorId)
                .name(name)
                .type(type).build();
    }

    public static ChannelCRUDDTO join(
            UUID userId,
            UUID channelId,
            ChannelType type
    ) {
        return ChannelCRUDDTO.builder()
                .creatorId(userId)
                .channelId(channelId)
                .type(type).build();
    }

    public static ChannelCRUDDTO delete(UUID serverId,
                                        UUID userId,
                                        UUID channelId) {
        return ChannelCRUDDTO.builder()
                .serverId(serverId)
                .creatorId(userId)
                .channelId(channelId).build();
    }

    public static ChannelCRUDDTO update(UUID replaceChannelId,
                                        String replaceName,
                                        ChannelType replaceType) {
        return ChannelCRUDDTO.builder()
                .serverId(replaceChannelId)
                .name(replaceName)
                .type(replaceType).build();
    }
}
