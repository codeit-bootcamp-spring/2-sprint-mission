package com.sprint.mission.discodeit.adapter.inbound.user.request;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

@Schema(description = "User 생성 정보")
public record UserCreateRequest(
    @Schema(description = "User name", example = "string")
    @NotBlank @Size(min = 1) String username,

    @Schema(description = "User email", example = "string")
    @NotBlank String email,

    @Schema(description = "User password", example = "string")
    @NotBlank @Size(min = 1) String password
) {

}
