package com.sprint.mission.discodeit.dto.channel;

import com.sprint.mission.discodeit.entity.channel.Channel;
import com.sprint.mission.discodeit.entity.channel.ChannelType;

import java.time.Instant;
import java.util.List;
import java.util.UUID;

public record ChannelFindResponse(
    UUID id,
    Instant createdAt,
    Instant updatedAt,
    ChannelType type,
    String name,
    String description,
    Instant lastMessageAt,
    List<UUID> participantIds

) {

  public static ChannelFindResponse fromEntity(Channel channel, Instant lastMessageAt,
      List<UUID> participantIds) {
    return new ChannelFindResponse(
        channel.getId(),
        channel.getCreatedAt(),
        channel.getUpdatedAt(),
        channel.getType(),
        channel.getName(),
        channel.getDescription(),
        lastMessageAt,
        participantIds
    );
  }
}
