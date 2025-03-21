package com.sprint.mission.discodeit.dto;

import com.sprint.mission.discodeit.entity.ChannelType;

import java.time.Instant;
import java.util.List;
import java.util.UUID;

public record ChannelDetailDto(
        UUID ChannelId,
        ChannelType type,
        String channelName,
        String description,
        Instant latestMessageTime,
        List<UUID> userId
) {}
