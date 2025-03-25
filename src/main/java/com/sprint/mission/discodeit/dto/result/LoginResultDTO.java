package com.sprint.mission.discodeit.dto.result;

import java.util.UUID;

public record LoginResultDTO(
        UUID id,
        String token
) {
}
