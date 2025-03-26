package com.sprint.mission.discodeit.dto.channel.common;

import com.sprint.mission.discodeit.entity.ChannelType;

import java.util.UUID;

public record ChannelUpdateDto(
        UUID id,
        String name,
        String description
) {}
