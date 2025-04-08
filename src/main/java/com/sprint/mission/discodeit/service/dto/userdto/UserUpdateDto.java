package com.sprint.mission.discodeit.service.dto.userdto;

import jakarta.validation.constraints.NotBlank;

public record UserUpdateDto(
        @NotBlank
        String newUsername,

        @NotBlank
        String newEmail,
        String newPassword
) {

}
