package com.sprint.mission.discodeit.dto.channel;

import java.time.Instant;
import java.util.List;
import java.util.UUID;

public record ChannelResponse(
    UUID id,
    String name,
    String description,
    Instant lastMessageAt,
    List<UUID> participantIds,
    String type,
    Instant createdAt,
    Instant updatedAt
) {

}
