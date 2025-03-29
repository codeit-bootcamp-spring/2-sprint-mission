package com.sprint.mission.discodeit.dto.controller.readstatus;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

import java.util.UUID;

public record CreateReadStatusRequestDTO(
        @NotNull (message = "userId를 입력해주세요.")
        UUID userId,
        @NotNull (message = "channelId를 입력해주세요.")
        UUID channelId
) {
}
