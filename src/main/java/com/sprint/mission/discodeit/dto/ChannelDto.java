package com.sprint.mission.discodeit.dto;

import com.sprint.mission.discodeit.constant.ChannelType;

import java.time.Instant;
import java.util.List;
import java.util.UUID;

public record ChannelDto(
        UUID channelUUID,
        String channelName,
        ChannelType channelType,
        List<UUID> joinUserId,
        Instant lastMessageTime
) {
    public ChannelDto(UUID channelUUID, String channelName, ChannelType channelType,Instant lastMessageTime) {
        this(channelUUID ,channelName, channelType, List.of(), lastMessageTime);
    }
}
