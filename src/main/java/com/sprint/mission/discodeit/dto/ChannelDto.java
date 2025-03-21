package com.sprint.mission.discodeit.dto;

import com.sprint.mission.discodeit.entity.ChannelType;

import java.util.List;
import java.util.UUID;

public record ChannelDto (
        UUID ChannelId,
        ChannelType type,
        String channelName,
        String description,
        List<UUID> userId
){ }