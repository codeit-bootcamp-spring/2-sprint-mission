package com.sprint.mission.discodeit.dto;

import java.time.Instant;
import java.util.Optional;
import java.util.UUID;

public record FindChannelDto(
        UUID channelUUID,
        String channelName,
        Optional<UUID> userUUID,
        Instant lastMessageTime
) {
    public FindChannelDto(UUID channelUUID, String channelName, Instant lastMessageTime) {
        this(channelUUID ,channelName, Optional.empty(), lastMessageTime);
    }

    public FindChannelDto(UUID channelUUID, String channelName, UUID userUUID, Instant lastMessageTime) {
        this(channelUUID, channelName, Optional.ofNullable(userUUID), lastMessageTime);
    }
}
