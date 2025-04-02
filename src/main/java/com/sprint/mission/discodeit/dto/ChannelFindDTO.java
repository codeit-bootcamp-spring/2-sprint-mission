package com.sprint.mission.discodeit.dto;

import com.sprint.mission.discodeit.entity.Channel;
import com.sprint.mission.discodeit.entity.ChannelType;
import lombok.Builder;

import java.time.Instant;
import java.util.List;
import java.util.UUID;

@Builder
public record ChannelFindDTO(
        UUID channelId,
        ChannelType type,
        String name,
        List<UUID> userIdList,
        Instant lastMessageAt
) {
    public static ChannelFindDTO create(Channel channel, List<UUID> userIdList, Instant lastMessageAt) {
        return ChannelFindDTO.builder()
                .channelId(channel.getChannelId())
                .type(channel.getType())
                .name(channel.getName())
                .userIdList(userIdList)
                .lastMessageAt(lastMessageAt)
                .build();
    }
}
