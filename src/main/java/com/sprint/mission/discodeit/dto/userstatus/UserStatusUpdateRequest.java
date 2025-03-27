package com.sprint.mission.discodeit.dto.userstatus;

import jakarta.validation.constraints.NotBlank;

import java.util.UUID;

public record UserStatusUpdateRequest(
        @NotBlank
        UUID id
) {
}
