package com.sprint.mission.discodeit.dto.readstatus;

import jakarta.validation.constraints.NotBlank;

import java.util.UUID;

public record ReadStatusUpdateRequest(
        @NotBlank
        UUID id
) {
}
