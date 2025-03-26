package com.sprint.mission.discodeit.dto.user;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

import java.util.UUID;

public record UserUpdateRequest (
        @NotNull
        UUID userId,

        @NotBlank
        String userName,

        @NotBlank
        String password,

        @NotNull
        UUID profileId
) {
}
