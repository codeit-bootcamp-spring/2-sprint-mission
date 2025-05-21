package com.sprint.mission.discodeit.dto.user;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

public record UserCreateRequest(
        @NotBlank
        @Size(max = 50)
        String username,

        @NotBlank
        @Size(max = 60)
        String password,

        @NotBlank
        @Email
        @Size(max = 100)
        String email
) {
}
