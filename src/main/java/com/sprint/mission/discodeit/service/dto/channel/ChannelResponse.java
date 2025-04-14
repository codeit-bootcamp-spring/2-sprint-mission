package com.sprint.mission.discodeit.service.dto.channel;

import com.sprint.mission.discodeit.entity.Channel;
import com.sprint.mission.discodeit.entity.ChannelType;
import java.time.Instant;
import java.util.List;
import java.util.UUID;

public record ChannelResponse(
    UUID id,
    ChannelType type,
    String name,
    String description,
    List<UUID> participants,
    Instant lastMessageAt
) {

  public static ChannelResponse of(Channel channel, List<UUID> participants,
      Instant lastMessageAt) {
    return new ChannelResponse(
        channel.getId(), channel.getType(),
        channel.getName(), channel.getDescription(), participants, lastMessageAt
    );
  }
}
