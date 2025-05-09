package com.sprint.mission.discodeit.dto.service.user;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.Size;

public record UserCreateRequest(
    @NotEmpty
    String username,

    @NotBlank
    @Email
    String email,

    @Size(min = 6, max = 20)
    @NotBlank
    String password
) {

}