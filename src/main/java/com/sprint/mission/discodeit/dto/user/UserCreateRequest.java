package com.sprint.mission.discodeit.dto.user;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

import java.util.UUID;

public record UserCreateRequest(
        @NotBlank
        String userName,

        @NotBlank
        @Email
        String userEmail,

        @NotBlank
        String password,

        @NotNull
        UUID profileId
) {
}
