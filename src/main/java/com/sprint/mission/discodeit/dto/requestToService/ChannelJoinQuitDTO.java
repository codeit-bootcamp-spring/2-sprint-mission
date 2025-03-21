package com.sprint.mission.discodeit.dto.requestToService;

import com.sprint.mission.discodeit.entity.ChannelType;
import lombok.Builder;

import java.util.UUID;

@Builder
public record ChannelJoinQuitDTO(
        UUID channelId,
        UUID userId,
        ChannelType type
) {
    public static ChannelJoinQuitDTO join(UUID channelId,
                                          UUID userId,
                                          ChannelType type) {
        return ChannelJoinQuitDTO.builder()
                .channelId(channelId)
                .userId(userId)
                .type(type).build();
    }

    public static ChannelJoinQuitDTO quit(UUID channelId,
                                          UUID userId) {
        return ChannelJoinQuitDTO.builder()
                .channelId(channelId)
                .userId(userId).build();
    }
}
