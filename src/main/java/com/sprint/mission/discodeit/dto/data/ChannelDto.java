package com.sprint.mission.discodeit.dto.data;

import com.sprint.mission.discodeit.entity.ChannelType;

import com.sprint.mission.discodeit.entity.UserChannel;
import java.time.Instant;
import java.util.List;
import java.util.Set;
import java.util.UUID;

public record ChannelDto(
    UUID id,
    ChannelType type,
    String name,
    String description,
    Set<UserChannel> participants,
    Instant lastMessageAt
) {

}
