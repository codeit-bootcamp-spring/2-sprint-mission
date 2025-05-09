package com.sprint.mission.discodeit.dto.service.user.userstatus;

import jakarta.validation.constraints.NotEmpty;
import java.time.Instant;

public record UserStatusUpdateRequest(
    @NotEmpty Instant newLastActiveAt
) {

}
