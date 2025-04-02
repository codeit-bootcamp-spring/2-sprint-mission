package com.sprint.mission.discodeit.adapter.inbound.channel.dto;

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
    List<UUID> userIdList,
    Instant lastMessageAt
) {

  public static ChannelResult create(Channel channel, List<UUID> userIdList,
      Instant lastMessageAt) {
    return ChannelResult.builder()
        .channelId(channel.getChannelId())
        .type(channel.getType())
        .name(channel.getName())
        .userIdList(userIdList)
        .lastMessageAt(lastMessageAt)
        .build();
  }
}
