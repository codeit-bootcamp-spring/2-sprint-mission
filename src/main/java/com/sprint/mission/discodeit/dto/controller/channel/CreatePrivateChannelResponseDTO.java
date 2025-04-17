package com.sprint.mission.discodeit.dto.controller.channel;

import com.sprint.mission.discodeit.dto.service.user.FindUserResult;
import com.sprint.mission.discodeit.entity.ChannelType;

import java.time.Instant;
import java.util.List;
import java.util.UUID;

public record CreatePrivateChannelResponseDTO(
    UUID id,
    ChannelType type,
    String name,
    String description,
    List<FindUserResult> participants,
    Instant lastMessageAt
) {

}
