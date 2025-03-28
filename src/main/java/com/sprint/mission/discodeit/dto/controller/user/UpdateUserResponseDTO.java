package com.sprint.mission.discodeit.dto.controller.user;

import com.sprint.mission.discodeit.dto.service.binarycontent.BinaryContentDTO;

import java.time.Instant;
import java.util.UUID;

public record UpdateUserResponseDTO(
        UUID id,
        BinaryContentDTO binaryContentDTO,
        Instant updatedAt,
        String username,
        String email
) {
}
