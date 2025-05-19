package com.sprint.mission.discodeit.dto.request;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.Size;

public record UserUpdateRequest(
    @Size(max = 20) String newUsername,
    @Email @Size(max = 20) String newEmail,
    @Size(max = 50) String newPassword
) {

}
