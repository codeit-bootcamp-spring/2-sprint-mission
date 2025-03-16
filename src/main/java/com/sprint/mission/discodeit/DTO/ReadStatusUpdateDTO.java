package com.sprint.mission.discodeit.DTO;

import java.util.UUID;

public record ReadStatusUpdateDTO(
        UUID readStatusId,
        UUID replaceId
) {
}
