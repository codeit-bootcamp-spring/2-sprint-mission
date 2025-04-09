package com.sprint.mission.discodeit.dto;

import com.sprint.mission.discodeit.entity.Channel;
import com.sprint.mission.discodeit.entity.ChannelType;

import java.time.Instant;
import java.util.Set;
import java.util.UUID;

public record ChannelResponseDto(
    UUID id,
    ChannelType type,
    String name,
    String description,
    Set<UUID> participantIds,
    Instant lastMessageCreatedAt
) {

  public static ChannelResponseDto convertToResponseDto(Channel channel, Set<UUID> participantIds,
      Instant lastMessageCreatedAt) {
    return new ChannelResponseDto(channel.getId(), channel.getType(),
        channel.getName(), channel.getDescription(), participantIds, lastMessageCreatedAt);
  }
  
}
