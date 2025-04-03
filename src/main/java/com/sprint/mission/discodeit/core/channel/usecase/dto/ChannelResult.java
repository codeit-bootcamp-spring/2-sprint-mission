package com.sprint.mission.discodeit.core.channel.usecase.dto;

import com.sprint.mission.discodeit.core.channel.entity.Channel;
import com.sprint.mission.discodeit.core.channel.entity.ChannelType;
import lombok.Builder;

import java.time.Instant;
import java.util.List;
import java.util.UUID;

@Builder
public record ChannelResult(
    UUID channelId,
    ChannelType type,
    String name,
    String description,
    List<UUID> userIdList,
    Instant lastMessageAt
) {

  public static ChannelResult create(Channel channel, List<UUID> userIdList,
      Instant lastMessageAt) {
    return ChannelResult.builder()
        .channelId(channel.getChannelId())
        .type(channel.getType())
        .name(channel.getName())
        .description(channel.getDescription())
        .userIdList(userIdList)
        .lastMessageAt(lastMessageAt)
        .build();
  }

}
