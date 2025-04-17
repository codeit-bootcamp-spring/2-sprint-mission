package com.sprint.mission.discodeit.service.dto.response;

import lombok.Builder;

import java.util.UUID;

@Builder
public record UserResponseDto(
        UUID id,
        String username,
        String email,
        BinaryContentResponseDto profile,
        Boolean online
) {

}
