package com.sprint.mission.discodeit.DTO.ReadStatus;

import lombok.Getter;

import java.time.Instant;
import java.util.UUID;

public record ReadStatusDto(
        UUID id,
        UUID userId,
        UUID channelId,
        Instant createdAt,
        Instant updatedAt,
        Instant lastReadAt
) {}

