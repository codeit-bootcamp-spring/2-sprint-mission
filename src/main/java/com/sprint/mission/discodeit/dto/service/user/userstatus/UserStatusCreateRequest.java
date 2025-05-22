package com.sprint.mission.discodeit.dto.service.user.userstatus;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotEmpty;
import java.time.Instant;
import java.util.UUID;

public record UserStatusCreateRequest(
    @NotBlank UUID userId,
    @NotEmpty Instant lastActiveAt
) {

}
