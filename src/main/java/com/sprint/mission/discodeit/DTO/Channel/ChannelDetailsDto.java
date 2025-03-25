package com.sprint.mission.discodeit.DTO.Channel;

import com.sprint.mission.discodeit.entity.ChannelType;

import java.time.Instant;
import java.util.List;
import java.util.UUID;

public record ChannelDetailsDto(
        UUID id,
        ChannelType type,
        String name,
        String description,
        Instant createdAt,
        Instant updatedAt,
        Instant latestMessageTime,
        List<UUID> participantUserIds
) {}
