package com.sprint.mission.discodeit.dto.request;

import jakarta.validation.constraints.NotNull;
import java.time.Instant;
import java.util.UUID;

public record UserStatusCreateRequest(

    @NotNull(message = "userId 필수 입력")
    UUID userId,

    @NotNull(message = "lastActiveAt 필수 입력")
    Instant lastActiveAt
) {

}
