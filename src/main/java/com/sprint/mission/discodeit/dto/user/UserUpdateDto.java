package com.sprint.mission.discodeit.dto.user;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.Size;

public record UserUpdateDto(
        @Size(max = 50)
        String newUsername,

        @Email
        @Size(max = 100)
        String newEmail,

        @Size(max = 60)
        String newPassword
) {
}
