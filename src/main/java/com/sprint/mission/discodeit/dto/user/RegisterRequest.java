package com.sprint.mission.discodeit.dto.user;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

public record RegisterRequest(

    @NotBlank
    @Size(max = 50)
    String username,

    @Email
    @NotBlank
    @Size(max = 100)
    String email,

    @NotBlank
    @Size(max = 60)
    String password
) {

}
