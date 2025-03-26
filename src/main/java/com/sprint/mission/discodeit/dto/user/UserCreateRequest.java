package com.sprint.mission.discodeit.dto.user;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;

import java.util.UUID;

public record UserCreateRequest(
        @NotBlank
        String userName,

        @NotBlank
        @Email
        String userEmail,

        @NotBlank
        String password,

        @NotBlank
        UUID profileId
) {
}
