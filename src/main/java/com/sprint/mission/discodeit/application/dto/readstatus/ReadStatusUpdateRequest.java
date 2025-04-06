package com.sprint.mission.discodeit.application.dto.readstatus;

import jakarta.validation.constraints.NotBlank;

public record ReadStatusUpdateRequest(@NotBlank String newLastReadAt) {
}
