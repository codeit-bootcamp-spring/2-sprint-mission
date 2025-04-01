package com.sprint.mission.discodeit.dto.auth;

import jakarta.validation.constraints.NotBlank;

public record AuthLoginRequest(
    String username,

    String password
) {

}
