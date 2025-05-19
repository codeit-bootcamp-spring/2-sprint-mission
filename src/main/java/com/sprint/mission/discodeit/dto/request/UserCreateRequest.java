package com.sprint.mission.discodeit.dto.request;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.Size;

public record UserCreateRequest(
    @NotEmpty @Size(max = 20) String username,
    @Email @NotBlank @Size(max = 20) String email,
    @NotBlank @Size(max = 50) String password
) {

}
