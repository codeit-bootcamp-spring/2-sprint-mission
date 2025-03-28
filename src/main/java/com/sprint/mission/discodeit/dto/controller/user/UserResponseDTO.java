package com.sprint.mission.discodeit.dto.controller.user;

import com.sprint.mission.discodeit.dto.service.binarycontent.BinaryContentDTO;

import java.time.Instant;
import java.util.UUID;

public record UserResponseDTO(
        UUID id,
        BinaryContentDTO binaryContentDTO,
        String username,
        String email,
        Instant createdAt,
        boolean isLogin
) {
}
