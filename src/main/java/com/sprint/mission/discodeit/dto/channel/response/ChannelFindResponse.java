package com.sprint.mission.discodeit.dto.channel.response;

import com.sprint.mission.discodeit.entity.ChannelType;

import java.time.Instant;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

public record ChannelFindResponse(
        ChannelType type,
        Instant lastMessageTime,
        String name,
        String description,
        List<UUID> userIds
) {
    public ChannelFindResponse(ChannelType type, Instant lastMessageTime, List<UUID> userIds) {
        this(type, lastMessageTime, "", "", userIds);
    }

    public ChannelFindResponse(ChannelType type, Instant lastMessageTime, String name, String description) {
        this(type, lastMessageTime, name, description, List.of());
    }
}
