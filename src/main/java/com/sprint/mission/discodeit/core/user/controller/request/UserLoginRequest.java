package com.sprint.mission.discodeit.core.user.controller.request;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;

@Schema(description = "User Login Request")
public record UserLoginRequest(
    @Schema(description = "User username", example = "string")
    @NotBlank String username,

    @Schema(description = "User password", example = "string")
    @NotBlank String password
) {

}
