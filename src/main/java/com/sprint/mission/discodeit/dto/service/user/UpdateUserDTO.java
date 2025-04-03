package com.sprint.mission.discodeit.dto.service.user;

import com.sprint.mission.discodeit.dto.service.binarycontent.BinaryContentDTO;

import java.time.Instant;
import java.util.UUID;

public record UpdateUserDTO(
        UUID id,
        BinaryContentDTO binaryContentDTO,
        Instant updatedAt,
        String username,
        String email
) {
}
