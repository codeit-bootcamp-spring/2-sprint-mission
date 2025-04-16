package com.sprint.mission.discodeit.dto.data;

import io.swagger.v3.oas.annotations.media.Schema;

import java.time.Instant;
import java.util.UUID;

@Schema(description = "사용자 응답 DTO")
public record UserDto(
        UUID id,
        Instant createdAt,
        Instant updatedAt,
        String username,
        String email,
        BinaryContentDto profileId,
        Boolean online
) {
}
