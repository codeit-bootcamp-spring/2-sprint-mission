package com.sprint.mission.discodeit.dto.data;

import java.time.OffsetDateTime;
import java.util.UUID;

public record UserDto(
    UUID id,
    OffsetDateTime createdAt,
    OffsetDateTime updatedAt,
    String username,
    String email,
    UUID profileId,
    Boolean online
) {

}
