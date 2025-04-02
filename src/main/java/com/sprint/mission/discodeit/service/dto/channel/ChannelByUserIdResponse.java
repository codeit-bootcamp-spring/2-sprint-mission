package com.sprint.mission.discodeit.service.dto.channel;

import com.sprint.mission.discodeit.entity.Channel;
import com.sprint.mission.discodeit.entity.ChannelType;
import java.time.Instant;
import java.util.List;
import java.util.UUID;

public record ChannelByUserIdResponse(
    UUID id,
    ChannelType type,
    String name,
    String description,
    List<UUID> participantIds,
    Instant lastMessageAt
) {

  public static ChannelByUserIdResponse of(Channel channel, String name, String description,
      List<UUID> participantIds,
      Instant lastMessageAt) {
    return new ChannelByUserIdResponse(
        channel.getId(), channel.getType(), name, description,
        participantIds, lastMessageAt
    );
  }
}
