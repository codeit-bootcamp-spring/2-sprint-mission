package com.sprint.mission.discodeit.dto.service.user;

import jakarta.validation.constraints.NotBlank;

public record LoginRequest(
    @NotBlank String username,
    @NotBlank String password
) {

}
