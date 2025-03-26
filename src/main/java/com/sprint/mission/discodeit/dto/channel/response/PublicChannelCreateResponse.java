package com.sprint.mission.discodeit.dto.channel.response;

import com.sprint.mission.discodeit.entity.ChannelType;

import java.time.Instant;
import java.util.UUID;

public record PublicChannelCreateResponse(
        ChannelType type,
        UUID id,
        String name,
        String description
) {}
