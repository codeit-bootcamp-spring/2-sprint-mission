package com.sprint.mission.discodeit.dto.user;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;

public record LoginRequest(

    @NotBlank
    @Size(max = 50)
    String username,

    @NotNull
    @Size(max = 60)
    String password
) {

}
