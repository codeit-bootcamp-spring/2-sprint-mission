package com.sprint.mission.discodeit.dto;

import com.sprint.mission.discodeit.constant.ChannelType;

import java.time.Instant;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

public record FindChannelDto(
        UUID channelUUID,
        String channelName,
        ChannelType channelType,
        List<UUID> joinUserId,
        Instant lastMessageTime
) {
    public FindChannelDto(UUID channelUUID, String channelName, ChannelType channelType,Instant lastMessageTime) {
        this(channelUUID ,channelName, channelType, List.of(), lastMessageTime);
    }
}
