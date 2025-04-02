package com.sprint.mission.discodeit.dto.controller.user;

import com.sprint.mission.discodeit.dto.service.binarycontent.BinaryContentDTO;

import java.util.UUID;

public record CreateUserResponseDTO(
        UUID id,
        BinaryContentDTO binaryContentDTO,
        String username,
        String email
) {
}
