package com.sprint.mission.discodeit.dto;

import java.util.UUID;

public record FindReadStatusByUserIdRequestDto(
        UUID userUUID
) {
}
