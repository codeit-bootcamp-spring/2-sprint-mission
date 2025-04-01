package com.sprint.mission.discodeit.dto;

import java.util.UUID;

public record UpdateReadStatusParamDto(
        UUID userUUID,
        UUID channelUUID
) {
}
