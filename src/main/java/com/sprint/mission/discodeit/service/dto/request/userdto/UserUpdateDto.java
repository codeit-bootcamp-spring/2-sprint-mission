package com.sprint.mission.discodeit.service.dto.request.userdto;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;

public record UserUpdateDto(
        @NotBlank String newUsername,
        @NotBlank @Email String newEmail,
        String newPassword
) {

}
