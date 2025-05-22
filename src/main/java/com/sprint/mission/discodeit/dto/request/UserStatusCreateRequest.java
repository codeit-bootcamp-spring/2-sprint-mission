package com.sprint.mission.discodeit.dto.request;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import java.time.Instant;
import java.util.UUID;

public record UserStatusCreateRequest(
    @NotNull(message = "userId는 필수 값입니다.")
    UUID userId,
    Instant lastActiveAt
) {

}
