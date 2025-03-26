package com.sprint.mission.discodeit.dto.service.readStatus;

import lombok.Builder;

import java.util.UUID;

@Builder
public record ReadStatusDTO(
        UUID id,
        UUID userId,
        UUID channelId
) {
}
