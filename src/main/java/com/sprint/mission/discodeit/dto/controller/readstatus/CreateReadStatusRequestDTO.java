package com.sprint.mission.discodeit.dto.controller.readstatus;

import jakarta.validation.constraints.NotBlank;

import java.util.UUID;

public record CreateReadStatusRequestDTO(
        @NotBlank
        UUID userId,
        @NotBlank
        UUID channelId
) {
}
