package com.sprint.mission.discodeit.dto;

import java.util.UUID;

public record SaveReadStatusParamDto(
        UUID userUUID,
        UUID channelUUID
) {
}
