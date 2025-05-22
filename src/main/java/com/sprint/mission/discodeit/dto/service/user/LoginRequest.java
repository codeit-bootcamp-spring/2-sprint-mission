package com.sprint.mission.discodeit.dto.service.user;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotEmpty;

public record LoginRequest(
    @NotEmpty String username,
    @NotBlank String password
) {

}
