package com.sprint.mission.discodeit.dto.service.user;

import com.sprint.mission.discodeit.dto.service.binarycontent.BinaryContentDTO;

import java.time.Instant;
import java.util.UUID;

public record UpdateUserDTO(
    UUID id,
    UUID profileId,
    Instant updatedAt,
    String username,
    String email,
    Boolean online
) {

}
