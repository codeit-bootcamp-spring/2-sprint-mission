package com.sprint.mission.discodeit.dto.service.user;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

public record UserUpdateRequest(
    @NotBlank
    String newUsername,

    @NotBlank
    @Email
    String newEmail,

    @Size(min = 6, max = 20)
    @NotBlank
    String newPassword
) {

}

