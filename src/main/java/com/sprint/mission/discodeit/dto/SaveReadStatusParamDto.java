package com.sprint.mission.discodeit.dto;

import java.util.UUID;

public record SaveReadStatusParamDto(
        UUID userId,
        UUID channelId
) {
}
