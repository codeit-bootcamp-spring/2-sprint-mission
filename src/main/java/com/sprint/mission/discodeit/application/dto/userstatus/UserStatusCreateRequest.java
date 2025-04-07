package com.sprint.mission.discodeit.application.dto.userstatus;

import io.swagger.v3.oas.annotations.media.Schema;

import java.time.Instant;
import java.util.UUID;

@Schema(description = "유저 상태 생성 요청")
public record UserStatusCreateRequest(
        UUID userId,
        Instant lastActiveAt
) {
}
