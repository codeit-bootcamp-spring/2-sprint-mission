package com.sprint.mission.discodeit.dto.channel;

import com.sprint.mission.discodeit.constant.ChannelType;
import com.sprint.mission.discodeit.dto.user.UserDto;
import java.time.Instant;
import java.util.List;
import java.util.UUID;

public record ChannelDto(
    UUID id,
    ChannelType type,
    String name,
    String description,
    List<UserDto> participantIds,
    Instant lastMessageAt
) {

  public ChannelDto(UUID id, ChannelType channelType, String name, String description,
      Instant lastMessageAt) {
    this(id, channelType, name, description, List.of(), lastMessageAt);
  }
}
