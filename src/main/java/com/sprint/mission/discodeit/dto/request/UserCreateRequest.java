package com.sprint.mission.discodeit.dto.request;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotNull;

public record UserCreateRequest(
    @NotNull
    String username,

    @NotNull
    @Email
    String email,

    @NotNull
    String password
) {

}
