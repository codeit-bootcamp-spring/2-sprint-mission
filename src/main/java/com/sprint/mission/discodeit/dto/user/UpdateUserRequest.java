package com.sprint.mission.discodeit.dto.user;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

public record UpdateUserRequest(

    @NotBlank
    @Size(max = 50)
    String newUsername,

    @NotBlank
    @Email
    @Size(max = 100)
    String newEmail,

    @NotBlank
    @Size(max = 60)
    String newPassword
) {

}
