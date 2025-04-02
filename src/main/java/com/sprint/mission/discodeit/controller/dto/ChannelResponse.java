package com.sprint.mission.discodeit.controller.dto;

import com.sprint.mission.discodeit.entity.Channel;
import com.sprint.mission.discodeit.entity.ChannelType;
import java.time.Instant;
import java.util.UUID;

public record ChannelResponse(
    UUID id,
    Instant createdAt,
    Instant updatedAt,
    ChannelType type,
    String name,
    String description
) {

  public static ChannelResponse of(Channel channel) {
    return new ChannelResponse(
        channel.getId(), channel.getCreatedAt(), channel.getUpdatedAt(), channel.getType(),
        channel.getName(), channel.getDescription()
    );
  }
}
