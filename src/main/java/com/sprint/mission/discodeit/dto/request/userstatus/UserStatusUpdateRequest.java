package com.sprint.mission.discodeit.dto.request.userstatus;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotNull;

import java.time.Instant;

@Schema(description = "유저 상태 수정요청")
public record UserStatusUpdateRequest(@NotNull Instant newLastActiveAt) {
}
