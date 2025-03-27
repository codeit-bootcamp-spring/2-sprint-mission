package com.sprint.mission.discodeit.application.dto.channel;

import com.sprint.mission.discodeit.entity.ChannelType;

import java.time.Instant;
import java.util.UUID;

public interface ChannelDto {
    UUID id();

    String name();

    ChannelType type();

    Instant lastMessageCreatedAt();
}