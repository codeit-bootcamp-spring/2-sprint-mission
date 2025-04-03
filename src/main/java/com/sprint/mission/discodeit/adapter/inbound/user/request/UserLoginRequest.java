package com.sprint.mission.discodeit.adapter.inbound.user.request;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;

@Schema(description = "User Login Request")
public record UserLoginRequest(
    @Schema(description = "User name", example = "string")
    @NotBlank String name,

    @Schema(description = "User password", example = "string")
    @NotBlank String password
) {

}
